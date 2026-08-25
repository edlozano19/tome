CREATE TABLE chapter (
    id UUID PRIMARY KEY,
    book_id UUID NOT NULL REFERENCES book (id) ON DELETE CASCADE,
    ordinal INT NOT NULL,
    title VARCHAR(500),
    spine_href VARCHAR(1000),
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT uq_chapter_book_ordinal UNIQUE (book_id, ordinal),
    CONSTRAINT ck_chapter_ordinal CHECK (ordinal >= 1)
);

CREATE INDEX idx_chapter_book_id ON chapter (book_id);

CREATE TABLE text_chunk (
    id UUID PRIMARY KEY,
    chapter_id UUID NOT NULL REFERENCES chapter (id) ON DELETE CASCADE,
    ordinal INT NOT NULL,
    body TEXT NOT NULL,
    tsv tsvector GENERATED ALWAYS AS (to_tsvector('english', coalesce(body, ''))) STORED,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT uq_text_chunk_chapter_ordinal UNIQUE (chapter_id, ordinal),
    CONSTRAINT ck_text_chunk_ordinal CHECK (ordinal >= 1)
);

CREATE INDEX idx_text_chunk_chapter_id ON text_chunk (chapter_id);
CREATE INDEX idx_text_chunk_tsv ON text_chunk USING GIN (tsv);

CREATE TABLE processed_event (
    event_id UUID NOT NULL,
    consumer_name VARCHAR(100) NOT NULL,
    processed_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT pk_processed_event PRIMARY KEY (event_id, consumer_name)
);