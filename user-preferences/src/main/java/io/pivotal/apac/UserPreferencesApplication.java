package io.pivotal.apac;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Configuration
@SpringBootApplication
public class UserPreferencesApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserPreferencesApplication.class, args);
	}

    String[] values = new String[] {"Sergiu","Flavio","Jeff","Arif","Karthik"};

//    @Bean
//    CommandLineRunner runner(final UserPreferenceRepository repository) {
//        return new CommandLineRunner() {
//            @Override
//            public void run(String... args) throws Exception {
//                for (String value : values) {
//                    UserPreference preference = new UserPreference();
//                    preference.userId = value;
//
//                    preference.categoryKey = "application";
//                    preference.categoryValue = "View";
//
//                    preference.categoryKey2 = "page";
//                    preference.categoryValue2 = "Transaction";
//
//                    preference.subCategoryKey = "tab";
//                    preference.subCategoryValue = "Social";
//
//
//                    preference.preferenceKey = "transactionTypeFilter";
//                    preference.preferenceValue = "All";
//                    repository.save(preference);
//                }
//                for (UserPreference preference : repository.findAll()) {
//                    System.out.println(preference);
//                }
//            }
//        };
//    }

}

@RestController
@RequestMapping("/api/v1")
class UserPreferencesController {

    @Autowired
    private UserPreferenceRepository userPreferenceRepository;

    @RequestMapping(method = RequestMethod.POST, value = "/userPreferences.json")
    ResponseEntity<String> saveUserPreference(@RequestBody UserPreference input) {
        //this.validateUser(userId);
        userPreferenceRepository.save(input);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/userPreferences.json")
    UserPreferenceView getUserPreference(
            @RequestParam("USERID") String userId) {

        Collection<UserPreference> preferences = userPreferenceRepository.findByUserId(userId);
        UserPreferenceView view = new UserPreferenceView();
        view.userId = userId;
        view.preferences = preferences;
        return view;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/users")
    List<UserPreference> getAllUserPreferences(
            @RequestParam("page") int page,
            @RequestParam("size") int size) {
        return userPreferenceRepository.findAll(new PageRequest(page, size)).getContent();
    }


    @RequestMapping(method = RequestMethod.DELETE, value = "/users/{userId}")
    void deleteUser(@PathVariable Long userId) {
        userPreferenceRepository.delete(userId);
    }

}

class UserPreferenceView{
    String userId;
    String action;
    Collection<UserPreference> preferences;

    public String getUserId() {
        return userId;
    }

    public String getAction() {
        return action;
    }

    public Collection<UserPreference> getPreferences() {
        return preferences;
    }
}

@Entity
@JsonInclude(JsonInclude.Include.NON_EMPTY)
class UserPreference {

    @JsonIgnore
    @javax.persistence.Id
    @GeneratedValue
    private Long Id;

    String userId;

    String categoryKey;
    String categoryValue;

    String categoryKey2;
    String categoryValue2;

    String subCategoryKey;
    String subCategoryValue;

    String subCategoryKey2;
    String subCategoryValue2;

    String preferenceKey;
    String preferenceValue;

    String preferenceKey2;

    String preferenceValue2;
    String binaryValue;

    Date lastModificationDate = new Date();

    String lastModificationUserId = "Sergiu";
    boolean isEnabled;

    boolean isEncrypted;

    @JsonIgnore
    public Long getId() {
        return Id;
    }

    public String getUserId() {
        return userId;
    }

    public String getCategoryKey() {
        return categoryKey;
    }

    public String getCategoryValue() {
        return categoryValue;
    }

    public String getCategoryKey2() {
        return categoryKey2;
    }

    public String getCategoryValue2() {
        return categoryValue2;
    }

    public String getSubCategoryKey() {
        return subCategoryKey;
    }

    public String getSubCategoryValue() {
        return subCategoryValue;
    }

    public String getSubCategoryKey2() {
        return subCategoryKey2;
    }

    public String getSubCategoryValue2() {
        return subCategoryValue2;
    }

    public String getPreferenceKey() {
        return preferenceKey;
    }

    public String getPreferenceValue() {
        return preferenceValue;
    }

    public String getPreferenceKey2() {
        return preferenceKey2;
    }

    public String getPreferenceValue2() {
        return preferenceValue2;
    }

    public String getBinaryValue() {
        return binaryValue;
    }

    public Date getLastModificationDate() {
        return lastModificationDate;
    }

    public String getLastModificationUserId() {
        return lastModificationUserId;
    }

    @JsonIgnore
    public boolean isEnabled() {
        return isEnabled;
    }

    @JsonIgnore
    public boolean isEncrypted() {
        return isEncrypted;
    }

    @Override
    public String toString() {
        return "UserPreference{" +
                "Id=" + Id +
                ", userId='" + userId + '\'' +
                ", categoryKey='" + categoryKey + '\'' +
                ", categoryValue='" + categoryValue + '\'' +
                ", categoryKey2='" + categoryKey2 + '\'' +
                ", categoryValue2='" + categoryValue2 + '\'' +
                ", subCategoryKey='" + subCategoryKey + '\'' +
                ", subCategoryValue='" + subCategoryValue + '\'' +
                ", subCategoryKey2='" + subCategoryKey2 + '\'' +
                ", subCategoryValue2='" + subCategoryValue2 + '\'' +
                ", preferenceKey='" + preferenceKey + '\'' +
                ", preferenceValue='" + preferenceValue + '\'' +
                ", preferenceKey2='" + preferenceKey2 + '\'' +
                ", preferenceValue2='" + preferenceValue2 + '\'' +
                ", binaryValue='" + binaryValue + '\'' +
                ", lastModificationDate=" + lastModificationDate +
                ", lastModificationUserId='" + lastModificationUserId + '\'' +
                ", isEnabled=" + isEnabled +
                ", isEncrypted=" + isEncrypted +
                '}';
    }

}

