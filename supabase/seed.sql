-- Seed data for local development (password: Test1234!)
-- Run after: supabase db push

insert into auth.users (
  id, email, encrypted_password, email_confirmed_at,
  created_at, updated_at,
  raw_app_meta_data, raw_user_meta_data,
  is_super_admin, role, aud
) values
  (
    '00000000-0000-0000-0000-000000000001',
    'alice@test.com',
    crypt('Test1234!', gen_salt('bf')),
    now(), now(), now(),
    '{"provider":"email","providers":["email"]}',
    '{"display_name":"Alice"}',
    false, 'authenticated', 'authenticated'
  ),
  (
    '00000000-0000-0000-0000-000000000002',
    'bob@test.com',
    crypt('Test1234!', gen_salt('bf')),
    now(), now(), now(),
    '{"provider":"email","providers":["email"]}',
    '{"display_name":"Bob"}',
    false, 'authenticated', 'authenticated'
  );

-- Profiles are auto-created by the trigger, but insert explicitly for seed clarity
insert into public.profiles (id, display_name, bio) values
  ('00000000-0000-0000-0000-000000000001', 'Alice', 'Test user Alice'),
  ('00000000-0000-0000-0000-000000000002', 'Bob', 'Test user Bob')
on conflict (id) do nothing;
