package com.tome.ingest.event;

import java.util.UUID;

public record BookUploaded(UUID eventId, UUID bookId, UUID bookFileId) {}
