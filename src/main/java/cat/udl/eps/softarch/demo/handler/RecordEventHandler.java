package cat.udl.eps.softarch.demo.handler;

import cat.udl.eps.softarch.demo.domain.Record;
import cat.udl.eps.softarch.demo.repository.RecordRepository;
import org.springframework.data.rest.core.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.security.core.context.SecurityContextHolder;
import cat.udl.eps.softarch.demo.domain.User;
import java.time.ZonedDateTime;

@Component
@RepositoryEventHandler
public class RecordEventHandler {
    final RecordRepository recordRepository;

    public RecordEventHandler(RecordRepository recordRepository) {
        this.recordRepository = recordRepository;
    }

    @HandleBeforeCreate
    public void handleRecordPreCreate(Record record) {

        // Validation métier
        if (record.getName() == null || record.getName().isBlank()) {
            throw new IllegalArgumentException("Record name cannot be empty");
        }

        ZonedDateTime timeStamp = ZonedDateTime.now();
        record.setCreated(timeStamp);
        record.setModified(timeStamp);
    }

    @HandleBeforeSave
    public void handleRecordPreSave(Record record) {

        User currentUser = (User) SecurityContextHolder
            .getContext()
            .getAuthentication()
            .getPrincipal();

        if (record.getOwnedBy() != null && !record.getOwnedBy().equals(currentUser)) {
            throw new IllegalArgumentException("You cannot modify this record");
        }

        ZonedDateTime timeStamp = ZonedDateTime.now();
        record.setModified(timeStamp);
    }

    @HandleBeforeDelete
    public void handleRecordPreDelete(Record record) {

        User currentUser = (User) SecurityContextHolder
            .getContext()
            .getAuthentication()
            .getPrincipal();

        if (record.getOwnedBy() == null || !record.getOwnedBy().equals(currentUser)) {
            throw new IllegalArgumentException("You cannot delete this record");
        }
    }
}
