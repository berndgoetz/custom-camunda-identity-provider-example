package berndgoetz.camunda.service;

import berndgoetz.camunda.engine.idservice.CustomGroupQuery;
import berndgoetz.camunda.engine.idservice.CustomUserQuery;
import berndgoetz.camunda.engine.idservice.IdentityService;
import jakarta.annotation.PostConstruct;
import org.camunda.bpm.engine.identity.Group;
import org.camunda.bpm.engine.identity.User;
import org.camunda.bpm.engine.impl.Page;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class IdentityServiceImpl implements IdentityService {

    private final Collection<Group> groups = new ArrayList<>();
    Collection<User> users = new java.util.ArrayList<>();
    Map<String, List<String>> userGroups = new java.util.HashMap<>();

    @PostConstruct
    public void init() {
        groups.add(new SimpleGroup("camunda-admin", "Camunda Cockpit Administrator", "SYSTEM"));
        groups.add(new SimpleGroup("admin", "Application Administrator", "APP"));
        groups.add(new SimpleGroup("businessservices", "Business Services", "ROLE"));
        groups.add(new SimpleGroup("team_a", "Team A", "TEAM"));
        groups.add(new SimpleGroup("team_b", "Team B", "TEAM"));
        groups.add(new SimpleGroup("team_c", "Team C", "TEAM"));
        users.add(new SimpleUser("admin", "Admin", "User", "admin@example.com", "password"));
        users.add(new SimpleUser("user", "Normal", "User", "normal@example.com", "password"));
        users.add(new SimpleUser("user2", "Normal Too", "User", "normal_too@example.com", "password"));
        userGroups.put("admin", List.of("camunda-admin", "admin"));
        userGroups.put("user", List.of("team_a", "team_b", "businessservices"));
        userGroups.put("user2", List.of("team_b"));
    }

    @Override
    public User findUserById(String id) {
        return users.stream()
                .filter(g -> g.getId().equals(id)).findFirst()
                .orElseThrow(() -> new java.util.NoSuchElementException("User not found with id: " + id));
    }

    @Override
    public Group findGroupById(String id) {
        return groups.stream()
                .filter(g -> g.getId().equals(id)).findFirst()
                .orElseThrow(() -> new java.util.NoSuchElementException("Group not found with id: " + id));
    }

    @Override
    public long countUsers(CustomUserQuery query) {
        return findUserByQueryCriteria(query, null).size();
    }

    @Override
    public long countGroups(CustomGroupQuery query) {
        return findGroupByQueryCriteria(query, null).size();
    }

    @Override
    public List<User> findUserByQueryCriteria(CustomUserQuery query, Page page) {
        if (query.getId() != null) {
            User user = findUserById(query.getId());
            if (user != null) {
                return List.of(user);
            } else {
                return Collections.emptyList();
            }
        }
        // TODO: support first, last, email, group id:
        var results = users.stream().toList();
        if (query.getFirstName() != null) {
            results = results.stream()
                    .filter(u -> u.getFirstName().equals(query.getFirstName()))
                    .toList();
        }
        if (query.getFirstNameLike() != null) {
            String firstNameLike = query.getFirstNameLike().replace("%", ".*").toLowerCase();
            results = results.stream()
                    .filter(u -> u.getFirstName().toLowerCase().matches(firstNameLike))
                    .toList();
        }
        if (query.getLastName() != null) {
            results = results.stream()
                    .filter(u -> u.getLastName().equals(query.getLastName()))
                    .toList();
        }
        if (query.getLastNameLike() != null) {
            String lastNameLike = query.getLastNameLike().replace("%", ".*").toLowerCase();
            results = results.stream()
                    .filter(u -> u.getLastName().toLowerCase().matches(lastNameLike))
                    .toList();
        }
        if (query.getEmail() != null) {
            results = results.stream()
                    .filter(u -> u.getEmail().equals(query.getEmail()))
                    .toList();
        }
        if (query.getEmailLike() != null) {
            String emailLike = query.getEmailLike().replace("%", ".*").toLowerCase();
            results = results.stream()
                    .filter(u -> u.getEmail().toLowerCase().matches(emailLike))
                    .toList();
        }
        if (query.getGroupId() != null) {
            String groupId = query.getGroupId();
            results = results.stream()
                    .filter(u -> {
                        List<String> groups = userGroups.get(u.getId());
                        return groups != null && groups.contains(groupId);
                    })
                    .toList();
        }
        return pagedList(results, page);
    }

    @Override
    public List<Group> findGroupByQueryCriteria(CustomGroupQuery query, Page page) {
        if (query.getId() != null) {
            Group group = findGroupById(query.getId());
            if (group != null) {
                return List.of(group);
            } else {
                return Collections.emptyList();
            }
        }
        // TODO: support name, name like, type, user:
        List<Group> results = groups.stream().toList();
        if (query.getName() != null) {
            results = results.stream()
                    .filter(g -> g.getName().equals(query.getName()))
                    .toList();
        }
        if (query.getNameLike() != null) {
            String nameLike = query.getNameLike().replace("%", ".*").toLowerCase();
            results = results.stream()
                    .filter(g -> g.getName().toLowerCase().matches(nameLike))
                    .toList();
        }
        if (query.getType() != null) {
            results = results.stream()
                    .filter(g -> g.getType().equals(query.getType()))
                    .toList();
        }
        if (query.getUserId() != null) {
            String userId = query.getUserId();
            List<String> groups = userGroups.get(userId);
            if (groups != null) {
                results = results.stream()
                        .filter(u -> {
                            return groups.contains(u.getId());
                        })
                        .toList();
            }
        }
        return pagedList(results, page);
    }

    private <T> List<T> pagedList(List<T> elements, Page page) {
        // Apply pagination if provided
        if (page != null) {
            int firstResult = page.getFirstResult();
            int maxResults = page.getMaxResults();
            // Handle edge cases
            if (firstResult >= elements.size()) {
                return Collections.emptyList();
            }
            int toIndex = Math.min(firstResult + maxResults, elements.size());
            return elements.subList(firstResult, toIndex);
        }
        return elements;
    }

}
