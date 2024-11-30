--
-- PostgreSQL database dump
--

-- Dumped from database version 14.12 (Homebrew)
-- Dumped by pg_dump version 16.3

-- Started on 2024-11-30 08:25:37 GMT

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
-- TOC entry 5 (class 2615 OID 2200)
-- Name: public; Type: SCHEMA; Schema: -; Owner: pradhyumyadav
--

CREATE SCHEMA public;


ALTER SCHEMA public OWNER TO pradhyumyadav;

--
-- TOC entry 3852 (class 0 OID 0)
-- Dependencies: 5
-- Name: SCHEMA public; Type: COMMENT; Schema: -; Owner: pradhyumyadav
--

COMMENT ON SCHEMA public IS 'standard public schema';


--
-- TOC entry 236 (class 1255 OID 16699)
-- Name: insert_booking(integer, integer, date, date, integer); Type: PROCEDURE; Schema: public; Owner: postgres
--

CREATE PROCEDURE public.insert_booking(IN p_user_id integer, IN p_trip_id integer, IN p_start_date date, IN p_end_date date, IN p_participants integer)
    LANGUAGE plpgsql
    AS $$
BEGIN
    -- Assuming availability check and business logic here
    INSERT INTO bookings (user_id, trip_id, start_date, end_date, number_of_participants)
    VALUES (p_user_id, p_trip_id, p_start_date, p_end_date, p_participants);
    
    -- You can add more logic to update trip status or handle errors
    COMMIT;
EXCEPTION WHEN OTHERS THEN
    -- Handle exceptions
    ROLLBACK;
END;
$$;


ALTER PROCEDURE public.insert_booking(IN p_user_id integer, IN p_trip_id integer, IN p_start_date date, IN p_end_date date, IN p_participants integer) OWNER TO postgres;

--
-- TOC entry 273 (class 1255 OID 17089)
-- Name: update_modified_column(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.update_modified_column() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$;


ALTER FUNCTION public.update_modified_column() OWNER TO postgres;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 231 (class 1259 OID 17041)
-- Name: adventures; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.adventures (
    id integer NOT NULL,
    description text NOT NULL
);


ALTER TABLE public.adventures OWNER TO postgres;

--
-- TOC entry 230 (class 1259 OID 17040)
-- Name: adventures_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.adventures_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.adventures_id_seq OWNER TO postgres;

--
-- TOC entry 3854 (class 0 OID 0)
-- Dependencies: 230
-- Name: adventures_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.adventures_id_seq OWNED BY public.adventures.id;


--
-- TOC entry 228 (class 1259 OID 16966)
-- Name: audit_log; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.audit_log (
    log_id integer NOT NULL,
    user_id integer NOT NULL,
    action_type character varying(50) NOT NULL,
    description text,
    affected_table character varying(50),
    "timestamp" timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE public.audit_log OWNER TO postgres;

--
-- TOC entry 227 (class 1259 OID 16965)
-- Name: audit_log_log_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.audit_log_log_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.audit_log_log_id_seq OWNER TO postgres;

--
-- TOC entry 3855 (class 0 OID 0)
-- Dependencies: 227
-- Name: audit_log_log_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.audit_log_log_id_seq OWNED BY public.audit_log.log_id;


--
-- TOC entry 215 (class 1259 OID 16633)
-- Name: bookings; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.bookings (
    booking_id integer NOT NULL,
    trip_id integer NOT NULL,
    user_id integer NOT NULL,
    num_participants integer NOT NULL,
    booking_date date DEFAULT CURRENT_DATE NOT NULL,
    customer_name character varying(255),
    customer_email character varying(255),
    customer_phone character varying(15),
    special_requests text,
    hotel_id integer,
    checkin_date date,
    checkout_date date,
    room_type character varying(255),
    status character varying(50) DEFAULT 'Pending'::character varying NOT NULL,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    total_price numeric(10,2)
);


ALTER TABLE public.bookings OWNER TO postgres;

--
-- TOC entry 214 (class 1259 OID 16632)
-- Name: bookings_booking_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.bookings_booking_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.bookings_booking_id_seq OWNER TO postgres;

--
-- TOC entry 3856 (class 0 OID 0)
-- Dependencies: 214
-- Name: bookings_booking_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.bookings_booking_id_seq OWNED BY public.bookings.booking_id;


--
-- TOC entry 233 (class 1259 OID 17050)
-- Name: hosts; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.hosts (
    host_id integer NOT NULL,
    name character varying(255) NOT NULL,
    email character varying(255) NOT NULL,
    phone character varying(20),
    description text,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    password character varying(255) DEFAULT 'alex123'::character varying NOT NULL
);


ALTER TABLE public.hosts OWNER TO postgres;

--
-- TOC entry 232 (class 1259 OID 17049)
-- Name: hosts_host_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.hosts_host_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.hosts_host_id_seq OWNER TO postgres;

--
-- TOC entry 3857 (class 0 OID 0)
-- Dependencies: 232
-- Name: hosts_host_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.hosts_host_id_seq OWNED BY public.hosts.host_id;


--
-- TOC entry 221 (class 1259 OID 16902)
-- Name: itinerary_item; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.itinerary_item (
    id integer NOT NULL,
    trip_id integer NOT NULL,
    day_number integer NOT NULL,
    title character varying(255) NOT NULL,
    description text
);


ALTER TABLE public.itinerary_item OWNER TO postgres;

--
-- TOC entry 220 (class 1259 OID 16901)
-- Name: itinerary_item_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.itinerary_item_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.itinerary_item_id_seq OWNER TO postgres;

--
-- TOC entry 3858 (class 0 OID 0)
-- Dependencies: 220
-- Name: itinerary_item_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.itinerary_item_id_seq OWNED BY public.itinerary_item.id;


--
-- TOC entry 235 (class 1259 OID 17124)
-- Name: offers; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.offers (
    id integer NOT NULL,
    trip_id integer NOT NULL,
    discounted_price numeric(10,2) NOT NULL,
    details text NOT NULL,
    is_active boolean DEFAULT true,
    offerphotos text NOT NULL,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    host_id integer NOT NULL
);


ALTER TABLE public.offers OWNER TO postgres;

--
-- TOC entry 234 (class 1259 OID 17123)
-- Name: offers_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.offers_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.offers_id_seq OWNER TO postgres;

--
-- TOC entry 3859 (class 0 OID 0)
-- Dependencies: 234
-- Name: offers_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.offers_id_seq OWNED BY public.offers.id;


--
-- TOC entry 217 (class 1259 OID 16656)
-- Name: roles; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.roles (
    role_id integer NOT NULL,
    role_name character varying(50) NOT NULL
);


ALTER TABLE public.roles OWNER TO postgres;

--
-- TOC entry 216 (class 1259 OID 16655)
-- Name: roles_role_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.roles_role_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.roles_role_id_seq OWNER TO postgres;

--
-- TOC entry 3860 (class 0 OID 0)
-- Dependencies: 216
-- Name: roles_role_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.roles_role_id_seq OWNED BY public.roles.role_id;


--
-- TOC entry 229 (class 1259 OID 16981)
-- Name: sales_data; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.sales_data (
    year integer NOT NULL,
    month character varying(50) NOT NULL,
    advertising_cost numeric(10,2),
    revenue numeric(10,2)
);


ALTER TABLE public.sales_data OWNER TO postgres;

--
-- TOC entry 219 (class 1259 OID 16759)
-- Name: trip_itinerary; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.trip_itinerary (
    id integer NOT NULL,
    trip_id integer NOT NULL,
    day_number integer NOT NULL,
    title character varying(255) NOT NULL,
    description text NOT NULL
);


ALTER TABLE public.trip_itinerary OWNER TO postgres;

--
-- TOC entry 218 (class 1259 OID 16758)
-- Name: trip_itinerary_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.trip_itinerary_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.trip_itinerary_id_seq OWNER TO postgres;

--
-- TOC entry 3861 (class 0 OID 0)
-- Dependencies: 218
-- Name: trip_itinerary_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.trip_itinerary_id_seq OWNED BY public.trip_itinerary.id;


--
-- TOC entry 226 (class 1259 OID 16954)
-- Name: trip_photos; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.trip_photos (
    photo_id integer NOT NULL,
    trip_id integer NOT NULL,
    photo_url character varying(255) NOT NULL
);


ALTER TABLE public.trip_photos OWNER TO postgres;

--
-- TOC entry 225 (class 1259 OID 16953)
-- Name: trip_photos_photo_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.trip_photos_photo_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.trip_photos_photo_id_seq OWNER TO postgres;

--
-- TOC entry 3862 (class 0 OID 0)
-- Dependencies: 225
-- Name: trip_photos_photo_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.trip_photos_photo_id_seq OWNED BY public.trip_photos.photo_id;


--
-- TOC entry 223 (class 1259 OID 16916)
-- Name: trip_reviews; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.trip_reviews (
    review_id integer NOT NULL,
    trip_id integer NOT NULL,
    user_id integer NOT NULL,
    rating integer NOT NULL,
    review_text text,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT trip_reviews_rating_check CHECK (((rating >= 1) AND (rating <= 5)))
);


ALTER TABLE public.trip_reviews OWNER TO postgres;

--
-- TOC entry 222 (class 1259 OID 16915)
-- Name: trip_reviews_review_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.trip_reviews_review_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.trip_reviews_review_id_seq OWNER TO postgres;

--
-- TOC entry 3863 (class 0 OID 0)
-- Dependencies: 222
-- Name: trip_reviews_review_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.trip_reviews_review_id_seq OWNED BY public.trip_reviews.review_id;


--
-- TOC entry 211 (class 1259 OID 16598)
-- Name: trips; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.trips (
    trip_id integer NOT NULL,
    destination character varying(255) NOT NULL,
    duration integer NOT NULL,
    price numeric(10,2) NOT NULL,
    activity_type character varying(255) NOT NULL,
    description text,
    start_date date,
    end_date date,
    max_participants integer,
    host_id integer,
    trip_name character varying(255),
    itinerary text,
    host_name character varying(255),
    host_contact_email character varying(255),
    host_contact_phone character varying(50),
    photos text,
    reviews jsonb,
    inclusions text,
    exclusions text,
    difficulty_level character varying(50),
    packing_list text,
    booking_deadline date,
    payment_terms text,
    cancellation_policy text
);


ALTER TABLE public.trips OWNER TO postgres;

--
-- TOC entry 210 (class 1259 OID 16597)
-- Name: trips_trip_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.trips_trip_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.trips_trip_id_seq OWNER TO postgres;

--
-- TOC entry 3864 (class 0 OID 0)
-- Dependencies: 210
-- Name: trips_trip_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.trips_trip_id_seq OWNED BY public.trips.trip_id;


--
-- TOC entry 224 (class 1259 OID 16938)
-- Name: user_roles; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.user_roles (
    user_id integer NOT NULL,
    role_id integer NOT NULL
);


ALTER TABLE public.user_roles OWNER TO postgres;

--
-- TOC entry 213 (class 1259 OID 16620)
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users (
    user_id integer NOT NULL,
    username character varying(255) DEFAULT NULL::character varying NOT NULL,
    password text,
    email character varying(255) NOT NULL,
    role character varying(50) DEFAULT 'USER'::character varying
);


ALTER TABLE public.users OWNER TO postgres;

--
-- TOC entry 212 (class 1259 OID 16619)
-- Name: users_user_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.users_user_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.users_user_id_seq OWNER TO postgres;

--
-- TOC entry 3865 (class 0 OID 0)
-- Dependencies: 212
-- Name: users_user_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.users_user_id_seq OWNED BY public.users.user_id;


--
-- TOC entry 3617 (class 2604 OID 17044)
-- Name: adventures id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.adventures ALTER COLUMN id SET DEFAULT nextval('public.adventures_id_seq'::regclass);


--
-- TOC entry 3615 (class 2604 OID 16969)
-- Name: audit_log log_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.audit_log ALTER COLUMN log_id SET DEFAULT nextval('public.audit_log_log_id_seq'::regclass);


--
-- TOC entry 3604 (class 2604 OID 16636)
-- Name: bookings booking_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.bookings ALTER COLUMN booking_id SET DEFAULT nextval('public.bookings_booking_id_seq'::regclass);


--
-- TOC entry 3618 (class 2604 OID 17122)
-- Name: hosts host_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hosts ALTER COLUMN host_id SET DEFAULT nextval('public.hosts_host_id_seq'::regclass);


--
-- TOC entry 3611 (class 2604 OID 16905)
-- Name: itinerary_item id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.itinerary_item ALTER COLUMN id SET DEFAULT nextval('public.itinerary_item_id_seq'::regclass);


--
-- TOC entry 3621 (class 2604 OID 17127)
-- Name: offers id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.offers ALTER COLUMN id SET DEFAULT nextval('public.offers_id_seq'::regclass);


--
-- TOC entry 3609 (class 2604 OID 16659)
-- Name: roles role_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.roles ALTER COLUMN role_id SET DEFAULT nextval('public.roles_role_id_seq'::regclass);


--
-- TOC entry 3610 (class 2604 OID 16762)
-- Name: trip_itinerary id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trip_itinerary ALTER COLUMN id SET DEFAULT nextval('public.trip_itinerary_id_seq'::regclass);


--
-- TOC entry 3614 (class 2604 OID 16957)
-- Name: trip_photos photo_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trip_photos ALTER COLUMN photo_id SET DEFAULT nextval('public.trip_photos_photo_id_seq'::regclass);


--
-- TOC entry 3612 (class 2604 OID 16919)
-- Name: trip_reviews review_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trip_reviews ALTER COLUMN review_id SET DEFAULT nextval('public.trip_reviews_review_id_seq'::regclass);


--
-- TOC entry 3600 (class 2604 OID 17067)
-- Name: trips trip_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trips ALTER COLUMN trip_id SET DEFAULT nextval('public.trips_trip_id_seq'::regclass);


--
-- TOC entry 3601 (class 2604 OID 16623)
-- Name: users user_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users ALTER COLUMN user_id SET DEFAULT nextval('public.users_user_id_seq'::regclass);


--
-- TOC entry 3842 (class 0 OID 17041)
-- Dependencies: 231
-- Data for Name: adventures; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.adventures (id, description) FROM stdin;
\.


--
-- TOC entry 3839 (class 0 OID 16966)
-- Dependencies: 228
-- Data for Name: audit_log; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.audit_log (log_id, user_id, action_type, description, affected_table, "timestamp") FROM stdin;
\.


--
-- TOC entry 3826 (class 0 OID 16633)
-- Dependencies: 215
-- Data for Name: bookings; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.bookings (booking_id, trip_id, user_id, num_participants, booking_date, customer_name, customer_email, customer_phone, special_requests, hotel_id, checkin_date, checkout_date, room_type, status, created_at, updated_at, total_price) FROM stdin;
1	7	3	2	2024-11-19	Pradhyum yadav	pradhyumyadav9522@gmail.com	7741354982	veg food	\N	\N	\N	\N	Confirmed	2024-11-19 19:39:28.23693	2024-11-19 19:39:28.23693	3798.00
2	7	3	2	2024-11-19	Tanu	tanu@gmail.com	7741354982	veg food but spicy	\N	\N	\N	\N	Confirmed	2024-11-19 19:49:53.118503	2024-11-19 19:49:53.118503	3798.00
3	7	3	1	2024-11-19	Aman	aman@gmail.com	9999999999	clean stay pls	\N	\N	\N	\N	Confirmed	2024-11-19 19:53:20.429283	2024-11-19 19:53:20.429283	1899.00
4	7	4	2	2024-11-21	\N	\N	\N	\N	\N	\N	\N	\N	Pending	2024-11-21 17:37:36.09636	2024-11-21 17:37:36.09636	\N
5	7	4	2	2024-11-21	pradatest	pradatest@alex.com	123	no	\N	\N	\N	\N	Pending	2024-11-21 17:44:43.765494	2024-11-21 17:44:43.765494	3798.00
6	7	4	2	2024-11-21	alexuser	alexuser@alex.com	123	no	\N	\N	\N	\N	Pending	2024-11-21 17:47:42.380219	2024-11-21 17:47:42.380219	3798.00
7	7	4	2	2024-11-21	alexuser2	alexuser2@alex.com	123	good food 	\N	\N	\N	\N	Pending	2024-11-21 17:58:14.973671	2024-11-21 17:58:14.973671	3798.00
8	7	4	3	2024-11-21	alexuser3	alexuser3@alex.com	1234567899	no request	\N	\N	\N	\N	Pending	2024-11-21 18:37:13.203751	2024-11-21 18:37:13.203751	5697.00
9	7	4	4	2024-11-21	alexuser4	alexuser4@alex.com	9999999999	please confirm booking fast	\N	\N	\N	\N	Pending	2024-11-21 18:46:38.608011	2024-11-21 18:46:38.608011	7596.00
10	7	4	1	2024-11-21	alextestuser90	alextestuser90@alex.com	98664342170	vegan food	\N	\N	\N	\N	Pending	2024-11-21 19:04:47.075457	2024-11-21 19:04:47.075457	1899.00
11	7	4	1	2024-11-21	john	john@alex.com	7877652456	BBQ Chicken preferred on outing	\N	\N	\N	\N	Pending	2024-11-21 19:09:32.271818	2024-11-21 19:09:32.271818	1899.00
17	10	4	2	2024-11-29	tanmay pal	paltanmay2023@gamil.com	7741354982	veg food	\N	\N	\N	\N	Pending	2024-11-29 01:11:07.040989	2024-11-29 01:11:07.040989	2000.00
18	10	4	2	2024-11-29	wdsd	dw@alex.com	827389273	nowso	\N	\N	\N	\N	Pending	2024-11-29 20:06:32.727264	2024-11-29 20:06:32.727264	2000.00
19	7	4	2	2024-11-30	gjyh	hgfj@alex.com	76786	nmbm	\N	\N	\N	\N	Pending	2024-11-30 03:46:06.80896	2024-11-30 03:46:06.80896	3798.00
\.


--
-- TOC entry 3844 (class 0 OID 17050)
-- Dependencies: 233
-- Data for Name: hosts; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.hosts (host_id, name, email, phone, description, created_at, password) FROM stdin;
1	John Doe	johndoe@example.com	123-456-7890	Experienced adventure guide with over 10 years of experience in mountain expeditions.	2024-11-18 00:54:27.143127	$2a$04$lckRq8LdxMiLJSGODrQQbezxOzOFgLvGPsTatZcjhrwc01GjxnLY6
2	Jane Smith	janesmith@example.com	123-456-7891	Cultural tour specialist with a deep knowledge of European history and arts.	2024-11-18 00:54:27.143127	$2a$04$lckRq8LdxMiLJSGODrQQbezxOzOFgLvGPsTatZcjhrwc01GjxnLY6
3	Alice Johnson	alicejohnson@example.com	123-456-7892	Enthusiastic wildlife guide, specializing in African safaris and wildlife conservation.	2024-11-18 00:54:27.143127	$2a$04$lckRq8LdxMiLJSGODrQQbezxOzOFgLvGPsTatZcjhrwc01GjxnLY6
4	Oliver Smith	oliver.smith@example.co.uk	+44 7911 123456	Specializes in countryside cottages and rural retreats.	2024-11-21 01:16:38.285826	$2a$04$lckRq8LdxMiLJSGODrQQbezxOzOFgLvGPsTatZcjhrwc01GjxnLY6
5	Amelia Jones	amelia.jones@example.co.uk	+44 7456 789123	Known for hosting luxury apartments in central London.	2024-11-21 01:16:38.285826	$2a$04$lckRq8LdxMiLJSGODrQQbezxOzOFgLvGPsTatZcjhrwc01GjxnLY6
6	Harry Brown	harry.brown@example.co.uk	+44 7812 345678	Expert in adventure lodges and activity-based stays.	2024-11-21 01:16:38.285826	$2a$04$lckRq8LdxMiLJSGODrQQbezxOzOFgLvGPsTatZcjhrwc01GjxnLY6
7	Isla Taylor	isla.taylor@example.co.uk	+44 7745 678912	Family-friendly vacation homes and seaside villas.	2024-11-21 01:16:38.285826	$2a$04$lckRq8LdxMiLJSGODrQQbezxOzOFgLvGPsTatZcjhrwc01GjxnLY6
8	George Wilson	george.wilson@example.co.uk	+44 7901 234567	Hosting exclusive penthouses and premium urban stays.	2024-11-21 01:16:38.285826	$2a$04$lckRq8LdxMiLJSGODrQQbezxOzOFgLvGPsTatZcjhrwc01GjxnLY6
101	John Doe	john.doe@example.com	1234567890	Bali beach resort expert	2024-11-21 09:57:31.271897	$2a$04$lckRq8LdxMiLJSGODrQQbezxOzOFgLvGPsTatZcjhrwc01GjxnLY6
102	Jane Smith	jane.smith@example.com	0987654321	Paris romantic getaway expert	2024-11-21 09:57:31.271897	$2a$04$lckRq8LdxMiLJSGODrQQbezxOzOFgLvGPsTatZcjhrwc01GjxnLY6
103	Chris Johnson	chris.johnson@example.com	5678901234	Alps hiking expert	2024-11-21 09:57:31.271897	$2a$04$lckRq8LdxMiLJSGODrQQbezxOzOFgLvGPsTatZcjhrwc01GjxnLY6
104	Alice Brown	alice.brown@example.com	6789012345	Dubai desert safari specialist	2024-11-21 09:57:31.271897	$2a$04$lckRq8LdxMiLJSGODrQQbezxOzOFgLvGPsTatZcjhrwc01GjxnLY6
105	Robert Wilson	robert.wilson@example.com	7890123456	New York city tour guide	2024-11-21 09:57:31.271897	$2a$04$lckRq8LdxMiLJSGODrQQbezxOzOFgLvGPsTatZcjhrwc01GjxnLY6
106	Emily Davis	emily.davis@example.com	8901234567	Venice gondola ride expert	2024-11-21 09:57:31.271897	$2a$04$lckRq8LdxMiLJSGODrQQbezxOzOFgLvGPsTatZcjhrwc01GjxnLY6
107	Michael Clark	michael.clark@example.com	9012345678	African safari organizer	2024-11-21 09:57:31.271897	$2a$04$lckRq8LdxMiLJSGODrQQbezxOzOFgLvGPsTatZcjhrwc01GjxnLY6
108	Olivia White	olivia.white@example.com	2345678901	Goa weekend planner	2024-11-21 09:57:31.271897	$2a$04$lckRq8LdxMiLJSGODrQQbezxOzOFgLvGPsTatZcjhrwc01GjxnLY6
109	Sophia Moore	sophia.moore@example.com	3456789012	Tokyo tour expert	2024-11-21 09:57:31.271897	$2a$04$lckRq8LdxMiLJSGODrQQbezxOzOFgLvGPsTatZcjhrwc01GjxnLY6
110	William Taylor	william.taylor@example.com	4567890123	Sydney Opera House expert	2024-11-21 09:57:31.271897	$2a$04$lckRq8LdxMiLJSGODrQQbezxOzOFgLvGPsTatZcjhrwc01GjxnLY6
9	AlexHost	alexHost@alex.com	\N	\N	2024-11-23 12:18:51.811837	$2a$10$NvfqgEo9L/7SzlFVNji7t.n0TMXwF1kJV9B5RlPDeYs8jU2KjlUbC
10	Pradhyum	pradhyumalex@alex.com	\N	\N	2024-11-26 14:35:50.261181	$2a$10$zL/5nN.USHFMJAKiP9zxxunB.Egk1P8rf/eqCE405uj6H2FOkKDsK
\.


--
-- TOC entry 3832 (class 0 OID 16902)
-- Dependencies: 221
-- Data for Name: itinerary_item; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.itinerary_item (id, trip_id, day_number, title, description) FROM stdin;
1	1	1	Day 1: Arrival	Arrive in the city and settle in the hotel
\.


--
-- TOC entry 3846 (class 0 OID 17124)
-- Dependencies: 235
-- Data for Name: offers; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.offers (id, trip_id, discounted_price, details, is_active, offerphotos, created_at, host_id) FROM stdin;
71	9	199.99	Tokyo Cherry Blossom Tour - Special Offer	t	https://example.com/images/tokyo_cherry_blossom.jpg	2024-11-21 09:57:37.262384	109
72	10	149.99	Sydney Opera House Experience - 25% Off	t	https://example.com/images/sydney_opera.jpg	2024-11-21 09:57:37.262384	110
63	1	199.99	Exclusive Bali Beach Resort Package - Save 30%!	t	https://example.com/images/bali_beach.jpg	2024-11-21 09:57:37.262384	1
64	2	159.99	Romantic Paris Getaway - Special Discount!	t	https://example.com/images/paris_getaway.jpg	2024-11-21 09:57:37.262384	1
65	3	249.99	Adventure in the Alps - 20% Off Hiking Trips!	t	https://example.com/images/alps_adventure.jpg	2024-11-21 09:57:37.262384	1
66	4	299.99	Luxury Dubai Desert Safari - Early Bird Offer!	t	https://example.com/images/dubai_safari.jpg	2024-11-21 09:57:37.262384	1
67	5	179.99	New York City Tour - Winter Sale	t	https://example.com/images/nyc_tour.jpg	2024-11-21 09:57:37.262384	1
68	6	139.99	Scenic Venice Gondola Ride - Exclusive Discount	t	https://example.com/images/venice_gondola.jpg	2024-11-21 09:57:37.262384	1
69	7	269.99	African Safari Adventure - Limited Time Offer	t	https://example.com/images/african_safari.jpg	2024-11-21 09:57:37.262384	1
70	8	89.99	Weekend Getaway to Goa - Hot Deal!	t	https://example.com/images/goa_weekend.jpg	2024-11-21 09:57:37.262384	1
73	10	500.00	enjoy indore at this new discounted price	t		2024-11-28 23:58:31.028287	10
\.


--
-- TOC entry 3828 (class 0 OID 16656)
-- Dependencies: 217
-- Data for Name: roles; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.roles (role_id, role_name) FROM stdin;
\.


--
-- TOC entry 3840 (class 0 OID 16981)
-- Dependencies: 229
-- Data for Name: sales_data; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.sales_data (year, month, advertising_cost, revenue) FROM stdin;
\.


--
-- TOC entry 3830 (class 0 OID 16759)
-- Dependencies: 219
-- Data for Name: trip_itinerary; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.trip_itinerary (id, trip_id, day_number, title, description) FROM stdin;
\.


--
-- TOC entry 3837 (class 0 OID 16954)
-- Dependencies: 226
-- Data for Name: trip_photos; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.trip_photos (photo_id, trip_id, photo_url) FROM stdin;
\.


--
-- TOC entry 3834 (class 0 OID 16916)
-- Dependencies: 223
-- Data for Name: trip_reviews; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.trip_reviews (review_id, trip_id, user_id, rating, review_text, created_at) FROM stdin;
4	1	1	5	Excellent trip!	2024-11-16 18:25:36.303866
\.


--
-- TOC entry 3822 (class 0 OID 16598)
-- Dependencies: 211
-- Data for Name: trips; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.trips (trip_id, destination, duration, price, activity_type, description, start_date, end_date, max_participants, host_id, trip_name, itinerary, host_name, host_contact_email, host_contact_phone, photos, reviews, inclusions, exclusions, difficulty_level, packing_list, booking_deadline, payment_terms, cancellation_policy) FROM stdin;
1	Tokyo, Japan	7	1999.00	Cultural	Join us for a 7-day adventure in Tokyo. From historic temples to modern skyscrapers, experience a blend of culture and innovation.	2025-04-15	2025-04-21	20	1	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
2	Oulanka National Park, Finland	8	2199.00	Multi-Activity Winter Adventure	Embark on an exhilarating 8-day journey through Finland's pristine Oulanka National Park. This adventure offers a blend of thrilling winter activities and serene natural beauty, providing an authentic Arctic experience.	2025-02-15	2025-02-22	20	1	Finnish Wilderness Adventure	Day 1: Arrival at Basecamp Oulanka. Settle into your cozy wooden lodge and enjoy a welcome dinner.\nDay 2: Guided snowshoe hike through snow-covered forests and along the scenic Kitka River.\nDay 3: Cross-country skiing session, exploring the tranquil trails around Juuma Lake.\nDay 4: Husky sledding adventure in Riisitunturi National Park, followed by an evening snowshoe excursion in search of the Northern Lights.\nDay 5: Free day to explore, relax in the sauna, or join optional activities like reindeer sledding.\nDay 6: Ice climbing session at Korouoma Canyon.\nDay 7: Another cross-country skiing session, ending with a farewell dinner at Basecamp Oulanka.\nDay 8: Departure after breakfast.	Arctic Adventures Finland	contact@arcticadventures.fi	+358 40 1234567	["https://via.placeholder.com/800x600?text=Finnish+Wilderness+1", \n      "https://via.placeholder.com/800x600?text=Finnish+Wilderness+2", \n      "https://via.placeholder.com/800x600?text=Finnish+Wilderness+3"]	[{"rating": 5, "comment": "A breathtaking experience! The husky sledding was the highlight of my trip.", "reviewer": "Alice Johnson"}, {"rating": 4, "comment": "Loved the Northern Lights excursion. The accommodation was comfortable and cozy.", "reviewer": "Mark Smith"}]	Accommodation, all meals, guides, and equipment for activities (snowshoes, skis, etc.).	International flights, travel insurance, optional activities (e.g., reindeer sledding).	Moderate	Warm winter clothing, waterproof hiking boots, thermal layers, gloves, hat, and scarf.	2025-02-01	50% payment upon booking, remaining balance due 7 days before the trip.	Full refund for cancellations made 14 days before departure. 50% refund for cancellations made 7 days before departure. No refund for cancellations made less than 7 days before departure.
3	Sagarmatha National Park, Nepal	14	1999.00	Hiking and Trekking	A challenging yet rewarding 14-day trek through the majestic Himalayas, leading you to the base camp of Mount Everest.	2025-03-05	2025-03-19	15	1	Everest Base Camp Trek	Day 1: Arrival in Kathmandu.\nDay 2: Flight to Lukla and trek to Phakding.\nDay 3: Trek to Namche Bazaar.\nDay 4: Acclimatization day in Namche Bazaar.\nDay 5: Trek to Tengboche.\nDay 6: Trek to Dingboche.\nDay 7: Acclimatization day in Dingboche.\nDay 8: Trek to Lobuche.\nDay 9: Trek to Everest Base Camp via Gorak Shep and return to Gorak Shep.\nDay 10: Hike to Kala Patthar and trek to Pheriche.\nDay 11: Trek to Namche Bazaar.\nDay 12: Trek to Lukla.\nDay 13: Flight back to Kathmandu.\nDay 14: Departure.	Himalayan Treks Nepal	info@himalayantreks.com	+977 1 5551234	["https://via.placeholder.com/800x600?text=Everest+Trek+1", \n      "https://via.placeholder.com/800x600?text=Everest+Trek+2", \n      "https://via.placeholder.com/800x600?text=Everest+Trek+3"]	[{"rating": 5, "comment": "The views were surreal, and the trek was a lifetime achievement!", "reviewer": "John Doe"}, {"rating": 4, "comment": "A physically demanding trek, but every step was worth it.", "reviewer": "Jane Smith"}]	Trekking permits, accommodation during the trek, experienced guide and porters.	International flights, visa fees, personal equipment, travel insurance.	Difficult	Trekking boots, thermal wear, down jacket, gloves, trekking poles, sunglasses.	2025-02-15	50% payment upon booking, remaining balance due 30 days before the trip.	Full refund for cancellations made 30 days before departure. 50% refund for cancellations made 15 days before departure. No refund for cancellations made less than 15 days before departure.
4	Torres del Paine National Park, Chile	10	2499.00	Hiking and Adventure	Experience the stunning wilderness of Patagonia in this 10-day adventure filled with breathtaking landscapes and challenging hikes.	2025-04-10	2025-04-20	12	1	Patagonia Explorer	Day 1: Arrival in Punta Arenas and transfer to Torres del Paine.\nDay 2: Trekking to the Base of the Towers.\nDay 3: Grey Glacier exploration and boat tour.\nDay 4: Trek to the French Valley.\nDay 5: Rest and exploration at the basecamp.\nDay 6: Visit Lago Nordenskjold and scenic viewpoints.\nDay 7: Trek to the Paine Circuit.\nDay 8: Optional kayaking or wildlife exploration.\nDay 9: Farewell dinner and reflections at the lodge.\nDay 10: Return to Punta Arenas and departure.	Patagonia Adventures Co.	info@patagoniaadventures.com	+56 2 1234567	["https://via.placeholder.com/800x600?text=Patagonia+Explorer+1", \n      "https://via.placeholder.com/800x600?text=Patagonia+Explorer+2", \n      "https://via.placeholder.com/800x600?text=Patagonia+Explorer+3"]	[{"rating": 5, "comment": "Absolutely stunning! The French Valley trek was my favorite.", "reviewer": "Emma Brown"}, {"rating": 4, "comment": "Well-organized trip with incredible scenery throughout.", "reviewer": "James Lee"}]	Accommodation, local transfers, meals, guides, and equipment.	International flights, travel insurance, personal expenses.	Moderate to Challenging	Hiking boots, waterproof jacket, thermal wear, gloves, sunglasses, backpack.	2025-03-20	50% payment upon booking, balance due 20 days before the trip.	Full refund for cancellations made 30 days prior. 50% refund for cancellations made 15 days prior. No refund for cancellations less than 15 days before the trip.
5	Sahara Desert, Morocco	7	1399.00	Cultural Exploration and Desert Safari	Immerse yourself in the vibrant culture of Morocco and the vast Sahara Desert during this 7-day journey.	2025-10-05	2025-10-12	20	1	Moroccan Desert Adventure	Day 1: Arrival in Marrakech and city tour.\nDay 2: Drive to Ait Benhaddou and visit the ancient Kasbahs.\nDay 3: Journey to the Sahara and camel trek to desert camp.\nDay 4: Sunrise in the desert and trek to Todra Gorge.\nDay 5: Visit Dades Valley and its rock formations.\nDay 6: Return to Marrakech and explore the souks.\nDay 7: Departure from Marrakech.	Sahara Experiences	contact@saharaexperiences.com	+212 6 1234567	["https://via.placeholder.com/800x600?text=Moroccan+Desert+1", \n      "https://via.placeholder.com/800x600?text=Moroccan+Desert+2", \n      "https://via.placeholder.com/800x600?text=Moroccan+Desert+3"]	[{"rating": 5, "comment": "The camel trek and desert camp were unforgettable experiences.", "reviewer": "Sophia Martin"}, {"rating": 4, "comment": "Loved the blend of culture and adventure on this trip.", "reviewer": "Liam Jones"}]	Accommodation, local transportation, meals, camel trek, and guided tours.	International flights, personal expenses, and travel insurance.	Easy to Moderate	Comfortable clothing, sunscreen, hat, walking shoes, backpack, scarf.	2025-09-20	Full payment upon booking.	Full refund for cancellations made 15 days before departure. No refund for cancellations made within 15 days of departure.
6	Amazon Basin, Peru	12	2999.00	Wildlife and Nature Exploration	Embark on a 12-day expedition into the heart of the Amazon Rainforest. Witness incredible biodiversity, local tribes, and pristine ecosystems.	2025-06-01	2025-06-12	15	1	Amazon Rainforest Expedition	Day 1: Arrival in Lima and orientation.\nDay 2: Flight to Iquitos and riverboat journey into the Amazon.\nDay 3: Explore the Pacaya-Samiria National Reserve.\nDay 4: Guided jungle trek and wildlife observation.\nDay 5: Visit a local indigenous community.\nDay 6: Canoeing through the flooded forest.\nDay 7: Birdwatching and piranha fishing.\nDay 8: Hike to a remote waterfall.\nDay 9: Night safari for nocturnal animals.\nDay 10: Relax at a jungle lodge.\nDay 11: Return to Iquitos and flight to Lima.\nDay 12: Departure from Lima.	Amazon Treks Peru	info@amazontreks.com	+51 1 9876543	["https://via.placeholder.com/800x600?text=Amazon+Rainforest+1", \n      "https://via.placeholder.com/800x600?text=Amazon+Rainforest+2", \n      "https://via.placeholder.com/800x600?text=Amazon+Rainforest+3"]	[{"rating": 5, "comment": "The best wildlife experience I've ever had. Saw jaguars and pink dolphins!", "reviewer": "Oliver Green"}, {"rating": 5, "comment": "The indigenous community visit was an eye-opening experience.", "reviewer": "Emily White"}]	Lodge accommodation, guided tours, riverboat transportation, meals.	International flights, personal items, optional activities, and travel insurance.	Moderate	Lightweight clothing, waterproof boots, insect repellent, sunscreen, binoculars, water bottle.	2025-05-15	50% payment upon booking, balance due 15 days before the trip.	Full refund for cancellations made 30 days before departure. No refund for cancellations made within 30 days of departure.
7	Petra, Jordan	7	1899.00	Exploration	Discover the ancient city of Petra, explore desert landscapes, and uncover the mysteries of Jordan's rich cultural heritage.	2025-09-10	2025-09-17	18	1	Lost Cities Exploration	Day 1: Arrival in Amman and welcome dinner.\nDay 2: Visit Jerash, one of the best-preserved Roman cities.\nDay 3: Drive to Petra with stops at Mount Nebo and Madaba.\nDay 4: Full-day exploration of Petra's archaeological sites.\nDay 5: Wadi Rum desert safari and Bedouin camp experience.\nDay 6: Relax at the Dead Sea and enjoy its therapeutic properties.\nDay 7: Departure from Amman.	Ancient Wonders Travel	info@ancientwonders.com	+962 6 1234567	["https://via.placeholder.com/800x600?text=Lost+Cities+Petra+1", \n      "https://via.placeholder.com/800x600?text=Lost+Cities+Petra+2", \n      "https://via.placeholder.com/800x600?text=Lost+Cities+Petra+3"]	[{"rating": 5, "comment": "Petra was beyond words! The Bedouin camp was magical.", "reviewer": "William Turner"}, {"rating": 4, "comment": "A fascinating trip. The Dead Sea experience was unforgettable.", "reviewer": "Ava Johnson"}]	Accommodation, local transportation, guided tours, meals, desert safari.	International flights, personal expenses, and travel insurance.	Easy to Moderate	Hiking boots, sunscreen, hat, comfortable clothing, swimwear, backpack.	2025-08-20	50% payment upon booking, balance due 10 days before the trip.	Full refund for cancellations made 20 days prior. No refund for cancellations made within 10 days of departure.
8	Tuscany, Italy	6	2599.00	Culinary Adventure	Savor the flavors of Tuscany on this 6-day culinary adventure. Enjoy cooking classes, vineyard tours, and farm-to-table meals.	2025-11-05	2025-11-11	12	1	Culinary Trails of Tuscany	Day 1: Arrival in Florence and welcome dinner with wine pairing.\nDay 2: Visit a local vineyard and learn about winemaking.\nDay 3: Hands-on cooking class in a Tuscan farmhouse.\nDay 4: Explore Siena and enjoy a traditional Tuscan meal.\nDay 5: Truffle hunting and olive oil tasting in San Gimignano.\nDay 6: Farewell brunch and departure from Florence.	Tuscan Flavors Tours	contact@tuscanflavors.com	+39 055 9876543	["https://via.placeholder.com/800x600?text=Tuscany+Culinary+1", \n      "https://via.placeholder.com/800x600?text=Tuscany+Culinary+2", \n      "https://via.placeholder.com/800x600?text=Tuscany+Culinary+3"]	[{"rating": 5, "comment": "The cooking class was incredible! The truffle hunt was a highlight.", "reviewer": "Isabella Rossi"}, {"rating": 5, "comment": "A food lover's dream! I learned so much and ate even more.", "reviewer": "Liam Evans"}]	Accommodation, meals, cooking classes, vineyard tours, and local transportation.	International flights, personal expenses, and travel insurance.	Easy	Comfortable clothing, walking shoes, reusable water bottle, notebook for recipes.	2025-10-15	Full payment upon booking.	Full refund for cancellations made 30 days prior. 50% refund for cancellations made 15 days prior. No refund for cancellations made within 15 days of departure.
9	Ujjain, MP, India	2	2000.00	Pilgrimage	A spiritual journey to Ujjain, visiting Mahakaleshwar temple and other religious spots.	2024-11-26	2024-11-28	7	1	Ujjain Darshan	Day 1: Arrival in Ujjain, Mahakaleshwar temple visit. Day 2: Explore Kal Bhairav temple and Shipra river.	\N	\N	\N	https://example.com/ujjain.jpg	\N	\N	\N	\N	\N	\N	\N	Full refund for cancellations made up to 48 hours before the trip start date.
10	Indore,MP,India	4	1000.00	Exploration	Testing description	2024-11-27	2024-11-30	10	10	Indore	blah blah blah	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	No refund cancelling before 1 Day.
11	Explore New City	2	200.00	Exploration	explore this new city which is in india	2024-11-28	2024-11-30	2	10	NewCity	\N	\N	\N	\N	/uploaded_photos/1732820995376_10_Luxury_hotel_with_pool.jpg	\N	\N	\N	\N	\N	\N	\N	50% refund
12	ded	2	200.00	Exploration	jdhkws	2024-11-28	2024-11-30	2	10	dcd	\N	\N	\N	\N	/uploaded_photos/10_1732827453772_BookingBackground.jpg	\N	\N	\N	\N	\N	\N	\N	dwd
13	destination demo	2	200.00	Exploration	Description	2024-11-29	2024-11-30	2	10	Test Trip	\N	\N	\N	\N	/uploaded_photos/10_1732836898428_IndoreCity.jpg	\N	\N	\N	\N	\N	\N	\N	dont cancel 
\.


--
-- TOC entry 3835 (class 0 OID 16938)
-- Dependencies: 224
-- Data for Name: user_roles; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.user_roles (user_id, role_id) FROM stdin;
\.


--
-- TOC entry 3824 (class 0 OID 16620)
-- Dependencies: 213
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.users (user_id, username, password, email, role) FROM stdin;
1	test_user	password	test_user@example.com	USER
2	Pradhyum	$2a$10$7KXFPnm0x0K0v1oj22NekeaMH6PESuUmsBwd0SXBn1DpO0o6nEzCm	pradhyumyadav992@gmail.com	USER
3	pradhyumtest	$2a$10$/ymcJhOMp3Ab3SV0Ax5t3udC09uPwXcD7p7Sjnf1YWpOGpm1xY0HW	pradhyumtest@gmail.com	USER
4	alex	$2a$10$wjo25kGyc8efOe1IfLPKG.xkmLi5MHD.kx4RxC1El5P7seJkJXgkS	alex@alex.com	USER
5	prada	$2a$10$sjdNnP1cCql4HxZcfs1b/OEQfYLwN4sSmUjycQxh14ePXCrNKIRDq	pradhyum9522@gmail.com	USER
\.


--
-- TOC entry 3866 (class 0 OID 0)
-- Dependencies: 230
-- Name: adventures_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.adventures_id_seq', 1, false);


--
-- TOC entry 3867 (class 0 OID 0)
-- Dependencies: 227
-- Name: audit_log_log_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.audit_log_log_id_seq', 1, false);


--
-- TOC entry 3868 (class 0 OID 0)
-- Dependencies: 214
-- Name: bookings_booking_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.bookings_booking_id_seq', 19, true);


--
-- TOC entry 3869 (class 0 OID 0)
-- Dependencies: 232
-- Name: hosts_host_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.hosts_host_id_seq', 10, true);


--
-- TOC entry 3870 (class 0 OID 0)
-- Dependencies: 220
-- Name: itinerary_item_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.itinerary_item_id_seq', 1, true);


--
-- TOC entry 3871 (class 0 OID 0)
-- Dependencies: 234
-- Name: offers_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.offers_id_seq', 73, true);


--
-- TOC entry 3872 (class 0 OID 0)
-- Dependencies: 216
-- Name: roles_role_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.roles_role_id_seq', 1, false);


--
-- TOC entry 3873 (class 0 OID 0)
-- Dependencies: 218
-- Name: trip_itinerary_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.trip_itinerary_id_seq', 1, false);


--
-- TOC entry 3874 (class 0 OID 0)
-- Dependencies: 225
-- Name: trip_photos_photo_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.trip_photos_photo_id_seq', 1, false);


--
-- TOC entry 3875 (class 0 OID 0)
-- Dependencies: 222
-- Name: trip_reviews_review_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.trip_reviews_review_id_seq', 4, true);


--
-- TOC entry 3876 (class 0 OID 0)
-- Dependencies: 210
-- Name: trips_trip_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.trips_trip_id_seq', 13, true);


--
-- TOC entry 3877 (class 0 OID 0)
-- Dependencies: 212
-- Name: users_user_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.users_user_id_seq', 5, true);


--
-- TOC entry 3664 (class 2606 OID 17048)
-- Name: adventures adventures_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.adventures
    ADD CONSTRAINT adventures_pkey PRIMARY KEY (id);


--
-- TOC entry 3660 (class 2606 OID 16974)
-- Name: audit_log audit_log_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.audit_log
    ADD CONSTRAINT audit_log_pkey PRIMARY KEY (log_id);


--
-- TOC entry 3640 (class 2606 OID 16639)
-- Name: bookings bookings_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.bookings
    ADD CONSTRAINT bookings_pkey PRIMARY KEY (booking_id);


--
-- TOC entry 3666 (class 2606 OID 17060)
-- Name: hosts hosts_email_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hosts
    ADD CONSTRAINT hosts_email_key UNIQUE (email);


--
-- TOC entry 3668 (class 2606 OID 17058)
-- Name: hosts hosts_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hosts
    ADD CONSTRAINT hosts_pkey PRIMARY KEY (host_id);


--
-- TOC entry 3650 (class 2606 OID 16909)
-- Name: itinerary_item itinerary_item_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.itinerary_item
    ADD CONSTRAINT itinerary_item_pkey PRIMARY KEY (id);


--
-- TOC entry 3670 (class 2606 OID 17133)
-- Name: offers offers_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.offers
    ADD CONSTRAINT offers_pkey PRIMARY KEY (id);


--
-- TOC entry 3644 (class 2606 OID 16661)
-- Name: roles roles_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.roles
    ADD CONSTRAINT roles_pkey PRIMARY KEY (role_id);


--
-- TOC entry 3646 (class 2606 OID 16663)
-- Name: roles roles_role_name_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.roles
    ADD CONSTRAINT roles_role_name_key UNIQUE (role_name);


--
-- TOC entry 3662 (class 2606 OID 16985)
-- Name: sales_data sales_data_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sales_data
    ADD CONSTRAINT sales_data_pkey PRIMARY KEY (year, month);


--
-- TOC entry 3648 (class 2606 OID 16766)
-- Name: trip_itinerary trip_itinerary_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trip_itinerary
    ADD CONSTRAINT trip_itinerary_pkey PRIMARY KEY (id);


--
-- TOC entry 3658 (class 2606 OID 16959)
-- Name: trip_photos trip_photos_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trip_photos
    ADD CONSTRAINT trip_photos_pkey PRIMARY KEY (photo_id);


--
-- TOC entry 3652 (class 2606 OID 17010)
-- Name: trip_reviews trip_reviews_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trip_reviews
    ADD CONSTRAINT trip_reviews_pkey PRIMARY KEY (review_id);


--
-- TOC entry 3629 (class 2606 OID 16937)
-- Name: trips trips_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trips
    ADD CONSTRAINT trips_pkey PRIMARY KEY (trip_id);


--
-- TOC entry 3632 (class 2606 OID 17030)
-- Name: users unique_email; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT unique_email UNIQUE (email);


--
-- TOC entry 3654 (class 2606 OID 17018)
-- Name: trip_reviews unique_trip_user; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trip_reviews
    ADD CONSTRAINT unique_trip_user UNIQUE (trip_id, user_id);


--
-- TOC entry 3656 (class 2606 OID 16942)
-- Name: user_roles user_roles_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_roles
    ADD CONSTRAINT user_roles_pkey PRIMARY KEY (user_id, role_id);


--
-- TOC entry 3634 (class 2606 OID 16631)
-- Name: users users_email_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_email_key UNIQUE (email);


--
-- TOC entry 3636 (class 2606 OID 16898)
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (user_id);


--
-- TOC entry 3638 (class 2606 OID 16629)
-- Name: users users_username_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_username_key UNIQUE (username);


--
-- TOC entry 3641 (class 1259 OID 16695)
-- Name: idx_bookings_trip_id; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_bookings_trip_id ON public.bookings USING btree (trip_id);


--
-- TOC entry 3642 (class 1259 OID 16694)
-- Name: idx_bookings_user_id; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_bookings_user_id ON public.bookings USING btree (user_id);


--
-- TOC entry 3625 (class 1259 OID 17070)
-- Name: trips_activity_type_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX trips_activity_type_idx ON public.trips USING btree (activity_type);


--
-- TOC entry 3626 (class 1259 OID 17071)
-- Name: trips_destination_activity_type_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX trips_destination_activity_type_idx ON public.trips USING btree (destination, activity_type);


--
-- TOC entry 3627 (class 1259 OID 17069)
-- Name: trips_destination_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX trips_destination_idx ON public.trips USING btree (destination);


--
-- TOC entry 3630 (class 1259 OID 17068)
-- Name: trips_trip_id_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX trips_trip_id_idx ON public.trips USING btree (trip_id);


--
-- TOC entry 3681 (class 2620 OID 17090)
-- Name: bookings update_bookings_modtime; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER update_bookings_modtime BEFORE UPDATE ON public.bookings FOR EACH ROW EXECUTE FUNCTION public.update_modified_column();


--
-- TOC entry 3678 (class 2606 OID 16975)
-- Name: audit_log audit_log_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.audit_log
    ADD CONSTRAINT audit_log_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(user_id) ON DELETE CASCADE;


--
-- TOC entry 3672 (class 2606 OID 16986)
-- Name: bookings fk_booking_trip; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.bookings
    ADD CONSTRAINT fk_booking_trip FOREIGN KEY (trip_id) REFERENCES public.trips(trip_id) ON DELETE CASCADE;


--
-- TOC entry 3673 (class 2606 OID 16991)
-- Name: bookings fk_booking_user; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.bookings
    ADD CONSTRAINT fk_booking_user FOREIGN KEY (user_id) REFERENCES public.users(user_id) ON DELETE CASCADE;


--
-- TOC entry 3671 (class 2606 OID 17061)
-- Name: trips fk_host_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trips
    ADD CONSTRAINT fk_host_id FOREIGN KEY (host_id) REFERENCES public.hosts(host_id);


--
-- TOC entry 3679 (class 2606 OID 17148)
-- Name: offers fk_offer_trip; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.offers
    ADD CONSTRAINT fk_offer_trip FOREIGN KEY (trip_id) REFERENCES public.trips(trip_id) ON DELETE CASCADE;


--
-- TOC entry 3674 (class 2606 OID 17020)
-- Name: itinerary_item fk_trip_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.itinerary_item
    ADD CONSTRAINT fk_trip_id FOREIGN KEY (trip_id) REFERENCES public.trips(trip_id) ON DELETE CASCADE;


--
-- TOC entry 3680 (class 2606 OID 17134)
-- Name: offers offers_host_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.offers
    ADD CONSTRAINT offers_host_id_fkey FOREIGN KEY (host_id) REFERENCES public.hosts(host_id) ON DELETE CASCADE;


--
-- TOC entry 3675 (class 2606 OID 17011)
-- Name: trip_reviews trip_reviews_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trip_reviews
    ADD CONSTRAINT trip_reviews_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(user_id) ON DELETE CASCADE;


--
-- TOC entry 3676 (class 2606 OID 16948)
-- Name: user_roles user_roles_role_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_roles
    ADD CONSTRAINT user_roles_role_id_fkey FOREIGN KEY (role_id) REFERENCES public.roles(role_id) ON DELETE CASCADE;


--
-- TOC entry 3677 (class 2606 OID 16943)
-- Name: user_roles user_roles_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_roles
    ADD CONSTRAINT user_roles_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(user_id) ON DELETE CASCADE;


--
-- TOC entry 3853 (class 0 OID 0)
-- Dependencies: 5
-- Name: SCHEMA public; Type: ACL; Schema: -; Owner: pradhyumyadav
--

REVOKE USAGE ON SCHEMA public FROM PUBLIC;
GRANT ALL ON SCHEMA public TO PUBLIC;


-- Completed on 2024-11-30 08:25:37 GMT

--
-- PostgreSQL database dump complete
--

