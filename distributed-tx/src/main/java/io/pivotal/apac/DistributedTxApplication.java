package io.pivotal.apac;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.transaction.Transactional;

@SpringBootApplication
public class DistributedTxApplication {

	public static void main(String[] args) throws Exception {
        SpringApplication.run(DistributedTxApplication.class, args).close();
	}

    @Bean
    CommandLineRunner runner(final AccountService service, final AccountRepository repository) {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                service.createAccountAndNotify("user-preferences");
                System.out.println("Count is " + repository.count());
                try {
                    service.createAccountAndNotify("error");
                }
                catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
                System.out.println("Count is " + repository.count());
            }
        };
    }
}



@Service
class AccountService {

	private final JmsTemplate jmsTemplate;
	private final AccountRepository accountRepository;

    @Autowired
	public AccountService(JmsTemplate jmsTemplate, AccountRepository accountRepository) {
		this.jmsTemplate = jmsTemplate;
		this.accountRepository = accountRepository;
	}

    @Transactional
	public void createAccountAndNotify(String username) {
		this.jmsTemplate.convertAndSend("accounts", username);
		this.accountRepository.save(new Account(username));
		if ("error".equals(username)) {
			throw new RuntimeException("Simulated error");
		}
	}

    @JmsListener(destination = "accounts")
    public void onMessage(String content) {
        System.out.println("----> " + content);
    }

}

@Entity
class Account {

    @Id
    @GeneratedValue
    private Long id;

    private String username;

    Account() {
    }

    public Account(String username) {
        this.username = username;
    }

}

interface AccountRepository extends JpaRepository<Account, Long> {

}