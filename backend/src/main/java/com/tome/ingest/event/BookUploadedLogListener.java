package com.tome.ingest.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@Slf4j
public class BookUploadedLogListener {

  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void onBookUploaded(BookUploaded event) {
    log.info(
        "BookUploaded eventId={} bookId={} bookFieldId={}",
        event.eventId(),
        event.bookId(),
        event.bookFileId());
  }
}
