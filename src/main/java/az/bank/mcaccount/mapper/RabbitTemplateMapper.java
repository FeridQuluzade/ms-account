package az.bank.mcaccount.mapper;

import az.bank.mcaccount.dto.MessageDto;
import az.bank.mcaccount.dto.client.ContactDto;
import az.bank.mcaccount.repository.model.AccountEntity;
import az.bank.mcaccount.repository.model.AccountHistoryEntity;
import az.bank.mcaccount.repository.model.AccountStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RabbitTemplateMapper {
    private final RabbitTemplate rabbitTemplate;

    public RabbitTemplateMapper(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendCreateMessage(ContactDto contactDto) {
        log.info("create ");

        rabbitTemplate.convertAndSend("MAIL_SENDER_Q",
                MessageDto.builder()
                        .email(contactDto.getContactValue())
                        .subject("Account information")
                        .text("Account has successfully created !")
                        .build()
        );
    }

    public void chanceStatusMessage(ContactDto contactDto,
                                    AccountStatus accountStatus){
        rabbitTemplate.convertAndSend("MAIL_SENDER_Q",
                MessageDto.builder()
                        .email(contactDto.getContactValue())
                        .subject("Account status information")
                        .text(String.format("Account has successfuly chance status ! \n Your status: %s ",accountStatus))
                        .build());
    }

    public void sendDeleteMessage(ContactDto contactDto) {
        rabbitTemplate.convertAndSend("MAIL_SENDER_Q",
                MessageDto.builder()
                        .email(contactDto.getContactValue())
                        .subject("Account information")
                        .text("Account has successfully deleted !")
                        .build());
    }

    public void sendEditMessage(AccountEntity accountEntity,
                                AccountHistoryEntity accountHistoryEntity,
                                ContactDto contactDto) {
        rabbitTemplate.convertAndSend("MAIL_SENDER_Q",
                MessageDto.builder()
                        .email(contactDto.getContactValue())
                        .subject("Account balance")
                        .text(String.format("Balance : %d , old balance %d , difference: %d",
                                accountEntity.getAmount(),
                                accountHistoryEntity.getAmount(),
                                accountEntity.getAmount()-accountHistoryEntity.getAmount()))
                        .build());
    }



}
