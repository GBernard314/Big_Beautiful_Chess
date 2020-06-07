--
-- PostgreSQL database dump
--

-- Dumped from database version 12.2
-- Dumped by pg_dump version 12.2

-- Started on 2020-06-07 17:26:40

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 2853 (class 1262 OID 16484)
-- Name: bbc; Type: DATABASE; Schema: -; Owner: bbc
--

CREATE DATABASE bbc WITH TEMPLATE = template0 ENCODING = 'UTF8' LC_COLLATE = 'French_France.1252' LC_CTYPE = 'French_France.1252';


ALTER DATABASE bbc OWNER TO bbc;

\connect bbc

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 202 (class 1259 OID 16485)
-- Name: authority; Type: TABLE; Schema: public; Owner: bbc
--

CREATE TABLE public.authority (
    id bigint NOT NULL,
    authority character varying(255)
);


ALTER TABLE public.authority OWNER TO bbc;

--
-- TOC entry 208 (class 1259 OID 16706)
-- Name: game; Type: TABLE; Schema: public; Owner: bbc
--

CREATE TABLE public.game (
    id bigint NOT NULL,
    board_info character varying(255),
    flag_winner integer,
    forfeit boolean,
    mouv character varying(255),
    nb_turn integer,
    storage character varying(255),
    time_user1 integer,
    time_user2 integer
);


ALTER TABLE public.game OWNER TO bbc;

--
-- TOC entry 207 (class 1259 OID 16532)
-- Name: game_users; Type: TABLE; Schema: public; Owner: bbc
--

CREATE TABLE public.game_users (
    game_id bigint NOT NULL,
    users_id bigint NOT NULL
);


ALTER TABLE public.game_users OWNER TO bbc;

--
-- TOC entry 205 (class 1259 OID 16498)
-- Name: hibernate_sequence; Type: SEQUENCE; Schema: public; Owner: bbc
--

CREATE SEQUENCE public.hibernate_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.hibernate_sequence OWNER TO bbc;

--
-- TOC entry 206 (class 1259 OID 16500)
-- Name: seq_authority; Type: SEQUENCE; Schema: public; Owner: bbc
--

CREATE SEQUENCE public.seq_authority
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.seq_authority OWNER TO bbc;

--
-- TOC entry 203 (class 1259 OID 16490)
-- Name: users; Type: TABLE; Schema: public; Owner: bbc
--

CREATE TABLE public.users (
    id bigint NOT NULL,
    email character varying(100),
    password character varying(100),
    username character varying(100),
    friends_list_id character varying(100)
);


ALTER TABLE public.users OWNER TO bbc;

--
-- TOC entry 204 (class 1259 OID 16495)
-- Name: users_authorities; Type: TABLE; Schema: public; Owner: bbc
--

CREATE TABLE public.users_authorities (
    user_id bigint NOT NULL,
    authorities_id bigint NOT NULL
);


ALTER TABLE public.users_authorities OWNER TO bbc;

--
-- TOC entry 2841 (class 0 OID 16485)
-- Dependencies: 202
-- Data for Name: authority; Type: TABLE DATA; Schema: public; Owner: bbc
--



--
-- TOC entry 2847 (class 0 OID 16706)
-- Dependencies: 208
-- Data for Name: game; Type: TABLE DATA; Schema: public; Owner: bbc
--

INSERT INTO public.game (id, board_info, flag_winner, forfeit, mouv, nb_turn, storage, time_user1, time_user2) VALUES (73, '4,0,1;6,1,0;6,2,0;6,3,0;6,5,0;6,6,0;6,7,0;/1,1,0;1,3,0;1,4,0;1,5,0;1,6,0;1,7,0;2,2,1;3,0,1;|7,7,0;/0,0,0;0,7,0;|5,2,1;7,6,0;/0,1,0;|3,1,1;7,2,0;/0,2,0;0,5,0;|7,3,0;/0,3,0;|7,4,0;/0,4,0;|', -1666, false, 'p:6,0-e:4,0;h:0,6-e:2,5;r:7,0-e:5,0;p:1,0-e:3,0;p:6,4-e:4,4;h:2,5-p:4,4;b:7,5-e:3,1;p:1,2-e:2,2;r:5,0-e:5,2;h:4,4-r:5,2;h:7,1-h:5,2;', 17, '2:19:39', 2400, 2400);
INSERT INTO public.game (id, board_info, flag_winner, forfeit, mouv, nb_turn, storage, time_user1, time_user2) VALUES (77, '4,1,1;4,2,1;4,5,1;4,6,1;6,0,0;6,3,0;6,4,0;6,7,0;/1,0,0;1,2,0;1,5,0;1,6,0;2,2,1;3,7,1;4,3,1;|7,0,0;7,7,0;/0,0,0;0,7,0;|7,1,0;/0,1,0;0,6,0;|7,2,0;7,5,0;/0,2,0;3,2,1;|7,3,0;/4,7,1;|7,4,0;/0,4,0;|', 2, false, 'h:7,6-e:5,5;p:1,4-e:3,4;h:5,5-p:3,4;b:0,5-e:3,2;h:3,4-e:2,2;p:1,3-e:3,3;p:6,1-e:4,1;p:3,3-e:4,3;p:6,2-e:4,2;p:1,1-h:2,2;p:6,6-e:4,6;p:1,7-e:3,7;p:6,5-e:4,5;q:0,3-e:4,7;', 20, '3:13:11', 2400, 2400);
INSERT INTO public.game (id, board_info, flag_winner, forfeit, mouv, nb_turn, storage, time_user1, time_user2) VALUES (78, '4,1,1;4,2,1;4,5,1;4,6,1;6,0,0;6,3,0;6,4,0;6,7,0;/1,0,0;1,2,0;1,5,0;1,6,0;2,2,1;3,7,1;4,3,1;|7,0,0;7,7,0;/0,0,0;0,7,0;|7,1,0;/0,1,0;0,6,0;|7,2,0;7,5,0;/0,2,0;3,2,1;|7,3,0;/4,7,1;|7,4,0;/0,4,0;|', 1, false, 'h:7,6-e:5,5;p:1,4-e:3,4;h:5,5-p:3,4;b:0,5-e:3,2;h:3,4-e:2,2;p:1,3-e:3,3;p:6,1-e:4,1;p:3,3-e:4,3;p:6,2-e:4,2;p:1,1-h:2,2;p:6,6-e:4,6;p:1,7-e:3,7;p:6,5-e:4,5;q:0,3-e:4,7;', 20, '3:13:11', 2400, 2400);
INSERT INTO public.game (id, board_info, flag_winner, forfeit, mouv, nb_turn, storage, time_user1, time_user2) VALUES (79, '4,1,1;4,2,1;4,5,1;4,6,1;6,0,0;6,3,0;6,4,0;6,7,0;/1,0,0;1,2,0;1,5,0;1,6,0;2,2,1;3,7,1;4,3,1;|7,0,0;7,7,0;/0,0,0;0,7,0;|7,1,0;/0,1,0;0,6,0;|7,2,0;7,5,0;/0,2,0;3,2,1;|7,3,0;/4,7,1;|7,4,0;/0,4,0;|', -666, false, 'h:7,6-e:5,5;p:1,4-e:3,4;h:5,5-p:3,4;b:0,5-e:3,2;h:3,4-e:2,2;p:1,3-e:3,3;p:6,1-e:4,1;p:3,3-e:4,3;p:6,2-e:4,2;p:1,1-h:2,2;p:6,6-e:4,6;p:1,7-e:3,7;p:6,5-e:4,5;q:0,3-e:4,7;', 20, '3:13:11', 2400, 2400);
INSERT INTO public.game (id, board_info, flag_winner, forfeit, mouv, nb_turn, storage, time_user1, time_user2) VALUES (80, '4,2,1;4,7,1;5,0,1;5,5,1;6,1,0;6,3,0;6,6,0;/1,0,0;1,1,0;1,7,0;2,3,1;3,2,1;3,4,1;3,6,1;|7,0,0;7,7,0;/0,0,0;0,7,0;|7,1,0;7,6,0;/0,1,0;2,7,1;|7,2,0;7,5,0;/0,2,0;4,1,1;|7,3,0;/0,3,0;|7,4,0;/0,5,1;|', -1666, false, 'p:6,2-e:4,2;h:0,6-e:2,7;p:6,7-e:4,7;p:1,4-e:3,4;p:6,5-e:5,5;b:0,5-e:4,1;p:6,4-e:5,4;p:1,5-e:3,5;p:5,4-e:4,4;p:1,2-e:3,2;p:4,4-p:3,5;p:1,3-e:2,3;p:3,5-e:2,5;p:1,6-e:3,6;p:2,5-e:1,5;k:0,4-e:1,4;p:1,5-e:0,5;k:1,4-q:0,5;p:6,0-e:5,0;', 31, '3:20:58', 2400, 2400);
INSERT INTO public.game (id, board_info, flag_winner, forfeit, mouv, nb_turn, storage, time_user1, time_user2) VALUES (81, '4,2,1;4,7,1;5,0,1;5,5,1;6,1,0;6,3,0;6,6,0;/1,0,0;1,1,0;1,7,0;2,3,1;3,2,1;3,4,1;3,6,1;|7,0,0;7,7,0;/0,0,0;0,7,0;|7,1,0;7,6,0;/0,1,0;2,7,1;|7,2,0;7,5,0;/0,2,0;4,1,1;|7,3,0;/0,3,0;|7,4,0;/0,5,1;|', -1666, false, 'p:6,2-e:4,2;h:0,6-e:2,7;p:6,7-e:4,7;p:1,4-e:3,4;p:6,5-e:5,5;b:0,5-e:4,1;p:6,4-e:5,4;p:1,5-e:3,5;p:5,4-e:4,4;p:1,2-e:3,2;p:4,4-p:3,5;p:1,3-e:2,3;p:3,5-e:2,5;p:1,6-e:3,6;p:2,5-e:1,5;k:0,4-e:1,4;p:1,5-e:0,5;k:1,4-q:0,5;p:6,0-e:5,0;', 31, '3:20:58', 2400, 2400);
INSERT INTO public.game (id, board_info, flag_winner, forfeit, mouv, nb_turn, storage, time_user1, time_user2) VALUES (82, '4,3,1;6,0,0;6,1,0;6,2,0;6,4,0;6,5,0;6,6,0;6,7,0;/1,0,0;1,1,0;1,2,0;1,3,0;1,4,0;1,5,0;1,6,0;1,7,0;|7,0,0;7,7,0;/0,0,0;0,7,0;|7,1,0;7,6,0;/0,1,0;0,6,0;|7,2,0;7,5,0;/0,2,0;0,5,0;|7,3,0;/0,3,0;|7,4,0;/0,4,0;|', -1, false, 'p:6,3-e:4,3;', 1, '3:31:50', 2400, 2400);


--
-- TOC entry 2846 (class 0 OID 16532)
-- Dependencies: 207
-- Data for Name: game_users; Type: TABLE DATA; Schema: public; Owner: bbc
--

INSERT INTO public.game_users (game_id, users_id) VALUES (32, 2);
INSERT INTO public.game_users (game_id, users_id) VALUES (32, 4);
INSERT INTO public.game_users (game_id, users_id) VALUES (39, 2);
INSERT INTO public.game_users (game_id, users_id) VALUES (39, 4);
INSERT INTO public.game_users (game_id, users_id) VALUES (40, 2);
INSERT INTO public.game_users (game_id, users_id) VALUES (40, 4);
INSERT INTO public.game_users (game_id, users_id) VALUES (41, 2);
INSERT INTO public.game_users (game_id, users_id) VALUES (41, 4);
INSERT INTO public.game_users (game_id, users_id) VALUES (42, 2);
INSERT INTO public.game_users (game_id, users_id) VALUES (42, 4);
INSERT INTO public.game_users (game_id, users_id) VALUES (43, 2);
INSERT INTO public.game_users (game_id, users_id) VALUES (43, 4);
INSERT INTO public.game_users (game_id, users_id) VALUES (44, 2);
INSERT INTO public.game_users (game_id, users_id) VALUES (44, 4);
INSERT INTO public.game_users (game_id, users_id) VALUES (45, 2);
INSERT INTO public.game_users (game_id, users_id) VALUES (45, 4);
INSERT INTO public.game_users (game_id, users_id) VALUES (46, 2);
INSERT INTO public.game_users (game_id, users_id) VALUES (46, 4);
INSERT INTO public.game_users (game_id, users_id) VALUES (73, 2);
INSERT INTO public.game_users (game_id, users_id) VALUES (73, 3);
INSERT INTO public.game_users (game_id, users_id) VALUES (77, 2);
INSERT INTO public.game_users (game_id, users_id) VALUES (77, 3);
INSERT INTO public.game_users (game_id, users_id) VALUES (78, 2);
INSERT INTO public.game_users (game_id, users_id) VALUES (78, 3);
INSERT INTO public.game_users (game_id, users_id) VALUES (79, 3);
INSERT INTO public.game_users (game_id, users_id) VALUES (79, 2);
INSERT INTO public.game_users (game_id, users_id) VALUES (80, 2);
INSERT INTO public.game_users (game_id, users_id) VALUES (80, 3);
INSERT INTO public.game_users (game_id, users_id) VALUES (81, 2);
INSERT INTO public.game_users (game_id, users_id) VALUES (81, 3);
INSERT INTO public.game_users (game_id, users_id) VALUES (82, 4);
INSERT INTO public.game_users (game_id, users_id) VALUES (82, 2);


--
-- TOC entry 2842 (class 0 OID 16490)
-- Dependencies: 203
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: bbc
--

INSERT INTO public.users (id, email, password, username, friends_list_id) VALUES (1, 'thibaudsimon@outlook.com', '$2a$10$fpDjnKlWNcGJN58TCbdn8OR/31Yes1Ee2zEMxa84Sfr..eH0FVf3i', 'TibRib', NULL);
INSERT INTO public.users (id, email, password, username, friends_list_id) VALUES (53, 'jony@gmail.com', '$2a$10$AZVknFxvwR4dWDnCr4zSbOdj4.MaN5.giy63XE65oYS/Xao0V4bve', 'jonnyjohn', '2;3;4');
INSERT INTO public.users (id, email, password, username, friends_list_id) VALUES (3, 'pierre@pierre.caillou', '$2a$10$cCXk0R5tiRoVy8lWsHaRmewBp0/iI4ZbUUsT7hqTVVI.KG9VdeLfG', 'pierre', '1;2;4;53');
INSERT INTO public.users (id, email, password, username, friends_list_id) VALUES (4, 'coco@co.co', '$2a$10$407Hr5rw0nPNVI3sdiNrqe5OElZLxyagffC2dbNNH2xvpCWn51ps2', 'coco', '1;2;3;53');
INSERT INTO public.users (id, email, password, username, friends_list_id) VALUES (74, 'bigboss@boss.mail', '$2a$10$73Rwa/x5GXlF7fREkR0q6OJHTFgIwfmdsbLN2nGD1DyTZZZT1uqx.', 'thebigboss', '2;3;4');
INSERT INTO public.users (id, email, password, username, friends_list_id) VALUES (75, 'new@new.co', '$2a$10$1nFVzqKra14O2qGicnVFSeP4skhQEQbO.FnCg5u1M0/vw1yU5xeOK', 'usernew', '');
INSERT INTO public.users (id, email, password, username, friends_list_id) VALUES (76, 'dzqd@dz.zz', '$2a$10$L9k1tQEAM14yhjnYpcFLEeouyJ1TndCdi9qpBft0qonK7AbVu6.ca', 'dz', '1;2');
INSERT INTO public.users (id, email, password, username, friends_list_id) VALUES (2, 'test@test.com', '$2a$10$/bv4z6LyO26BgL7WXilGnO.NZlexXd0jljvQDWC2vqqdIGiF/Mnmm', 'test', '1;3;4;53;76');


--
-- TOC entry 2843 (class 0 OID 16495)
-- Dependencies: 204
-- Data for Name: users_authorities; Type: TABLE DATA; Schema: public; Owner: bbc
--



--
-- TOC entry 2854 (class 0 OID 0)
-- Dependencies: 205
-- Name: hibernate_sequence; Type: SEQUENCE SET; Schema: public; Owner: bbc
--

SELECT pg_catalog.setval('public.hibernate_sequence', 82, true);


--
-- TOC entry 2855 (class 0 OID 0)
-- Dependencies: 206
-- Name: seq_authority; Type: SEQUENCE SET; Schema: public; Owner: bbc
--

SELECT pg_catalog.setval('public.seq_authority', 1, false);


--
-- TOC entry 2707 (class 2606 OID 16489)
-- Name: authority authority_pkey; Type: CONSTRAINT; Schema: public; Owner: bbc
--

ALTER TABLE ONLY public.authority
    ADD CONSTRAINT authority_pkey PRIMARY KEY (id);


--
-- TOC entry 2711 (class 2606 OID 16713)
-- Name: game game_pkey; Type: CONSTRAINT; Schema: public; Owner: bbc
--

ALTER TABLE ONLY public.game
    ADD CONSTRAINT game_pkey PRIMARY KEY (id);


--
-- TOC entry 2709 (class 2606 OID 16494)
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: bbc
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- TOC entry 2714 (class 2606 OID 16537)
-- Name: game_users fk7ld7p8yxteoc8lqi1kpst2o15; Type: FK CONSTRAINT; Schema: public; Owner: bbc
--

ALTER TABLE ONLY public.game_users
    ADD CONSTRAINT fk7ld7p8yxteoc8lqi1kpst2o15 FOREIGN KEY (users_id) REFERENCES public.users(id);


--
-- TOC entry 2713 (class 2606 OID 16507)
-- Name: users_authorities fkq3lq694rr66e6kpo2h84ad92q; Type: FK CONSTRAINT; Schema: public; Owner: bbc
--

ALTER TABLE ONLY public.users_authorities
    ADD CONSTRAINT fkq3lq694rr66e6kpo2h84ad92q FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- TOC entry 2712 (class 2606 OID 16502)
-- Name: users_authorities fkt25vmk46t0o0x01yo0wyx7wmf; Type: FK CONSTRAINT; Schema: public; Owner: bbc
--

ALTER TABLE ONLY public.users_authorities
    ADD CONSTRAINT fkt25vmk46t0o0x01yo0wyx7wmf FOREIGN KEY (authorities_id) REFERENCES public.authority(id);


-- Completed on 2020-06-07 17:26:40

--
-- PostgreSQL database dump complete
--

