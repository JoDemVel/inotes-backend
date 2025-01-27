create table users (
    id bigint primary key generated always as identity,
    username text not null,
    email text not null unique,
    password_hash text,
    created_at timestamptz default now(),
    updated_at timestamptz default now()
);

create table notes (
    id bigint primary key generated always as identity,
    user_id bigint references users (id) on delete cascade,
    title text not null,
    content text,
    is_archived boolean default false,
    created_at timestamptz default now(),
    updated_at timestamptz default now()
);

create table tags (
    id bigint primary key generated always as identity,
    user_id bigint references users (id) on delete cascade,
    name text not null,
    unique (user_id, name)
);

create table note_tags (
    note_id bigint references notes (id) on delete cascade,
    tag_id bigint references tags (id) on delete cascade,
    primary key (note_id, tag_id)
);

create table user_preferences (
    user_id bigint primary key references users (id) on delete cascade,
    filter_state jsonb,
    search_state jsonb
);

create index idx_notes_title on notes using gin (to_tsvector('simple', title));

create index idx_notes_content on notes using gin (to_tsvector('simple', content));

create index idx_tags_name on tags using gin (to_tsvector('simple', name));

create table roles (
    id bigint primary key generated always as identity,
    name text not null unique
);

create table permissions (
    id bigint primary key generated always as identity,
    name text not null unique
);

create table user_roles (
    user_id bigint references users (id) on delete cascade,
    role_id bigint references roles (id) on delete cascade,
    primary key (user_id, role_id)
);

create table role_permissions (
    role_id bigint references roles (id) on delete cascade,
    permission_id bigint references permissions (id) on delete cascade,
    primary key (role_id, permission_id)
);
