CREATE TABLE book (
    id UUID PRIMARY KEY,
    title VARCHAR(500) NOT NULL,
    author VARCHAR(300) NOT NULL,
    slug VARCHAR(200) NOT NULL,
    description TEXT,
    language VARCHAR(16) NOT NULL DEFAULT 'en',
    is_public_domain BOOLEAN NOT NULL DEFAULT TRUE,
    source VARCHAR(100),
    cover_url VARCHAR(1000),
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT uq_book_slug UNIQUE (slug)
);

CREATE TABLE book_file (
    id UUID PRIMARY KEY,
    book_id UUID NOT NULL REFERENCES book (id) ON DELETE CASCADE,
    sha256 CHAR(64) NOT NULL,
    original_filename VARCHAR(500),
    content_type VARCHAR(100) NOT NULL DEFAULT 'application/epub+zip',
    size_bytes BIGINT NOT NULL,
    storage_path VARCHAR(1000) NOT NULL,
    ingest_status VARCHAR(32) NOT NULL DEFAULT 'PENDING',
    ingest_error TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT uq_book_file_book_id UNIQUE (book_id),
    CONSTRAINT uq_book_file_sha256 UNIQUE (sha256),
    CONSTRAINT ck_book_file_ingest_status
        CHECK (ingest_status IN ('PENDING', 'PARSING', 'READY', 'FAILED'))
);

CREATE TABLE user_book (
    id UUID PRIMARY KEY,
    account_id UUID NOT NULL REFERENCES account (id) ON DELETE CASCADE,
    book_id UUID NOT NULL REFERENCES book (id) ON DELETE CASCADE,
    status VARCHAR(32) NOT NULL DEFAULT 'WANT_TO_READ',
    rating SMALLINT,
    current_chapter INT,
    current_cfi VARCHAR(500),
    added_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT uq_user_book_account_book UNIQUE (account_id, book_id),
    CONSTRAINT ck_user_book_status
        CHECK (status IN ('WANT_TO_READ', 'READING', 'COMPLETED', 'PAUSED', 'ABANDONED')),
    CONSTRAINT ck_user_book_rating
        CHECK (rating IS NULL OR (rating >= 1 AND rating <= 5))
);

CREATE INDEX idx_user_book_account_id ON user_book (account_id);
CREATE INDEX idx_user_book_book_id ON user_book (book_id);