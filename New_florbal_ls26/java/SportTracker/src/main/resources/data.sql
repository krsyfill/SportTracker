-- =========================================
-- RESET DATABASE
-- =========================================

TRUNCATE TABLE attendance RESTART IDENTITY CASCADE;
TRUNCATE TABLE trainings RESTART IDENTITY CASCADE;
TRUNCATE TABLE players RESTART IDENTITY CASCADE;

-- =========================================
-- PLAYERS
-- =========================================

INSERT INTO players
(first_name, last_name, phone, birth_date, note)

VALUES

    (
        'Filip',
        'Novák',
        '+420777123456',
        '2000-05-12',
        'Kapitán týmu'
    ),

    (
        'Petr',
        'Svoboda',
        '+420777654321',
        '2001-08-25',
        'Brankář'
    ),

    (
        'Tomáš',
        'Dvořák',
        '+420777987654',
        '2003-01-17',
        'Nový hráč'
    ),

    (
        'Martin',
        'Král',
        '+420777111222',
        '1999-11-03',
        'Obránce'
    ),

    (
        'Jakub',
        'Procházka',
        '+420777333444',
        '2002-07-14',
        'Útočník'
    );

-- =========================================
-- TRAININGS
-- =========================================

INSERT INTO trainings (training_date)

VALUES

    -- PAST TRAININGS

    ('2026-01-10'),
    ('2026-01-12'),
    ('2026-01-15'),
    ('2026-01-18'),
    ('2026-01-22'),
    ('2026-01-25'),

    -- UPCOMING TRAININGS

    ('2026-06-01'),
    ('2026-06-03'),
    ('2026-06-05'),
    ('2026-06-08'),
    ('2026-06-10');

-- =========================================
-- ATTENDANCE
-- =========================================

INSERT INTO attendance
(planned_attendance,
 actual_attendance,
 player_id,
 training_id)

VALUES

    -- TRAINING 1

    (true, true, 1, 1),
    (true, true, 2, 1),
    (true, false, 3, 1),
    (true, true, 4, 1),

    -- TRAINING 2

    (true, true, 1, 2),
    (true, true, 3, 2),
    (false, false, 5, 2),

    -- TRAINING 3

    (true, true, 2, 3),
    (true, false, 4, 3),
    (true, true, 5, 3),

    -- TRAINING 4

    (true, true, 1, 4),
    (true, true, 2, 4),
    (true, true, 3, 4),
    (true, false, 5, 4),

    -- TRAINING 5

    (true, true, 1, 5),
    (true, false, 2, 5),
    (true, true, 4, 5),

    -- TRAINING 6

    (true, true, 3, 6),
    (true, true, 4, 6),
    (true, true, 5, 6);