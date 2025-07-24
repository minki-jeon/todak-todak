select now();
SELECT VERSION();

# 회원관리
CREATE TABLE member
(
    seq         INT AUTO_INCREMENT NOT NULL,
    member_no   VARCHAR(20)        NOT NULL UNIQUE,
    auth        VARCHAR(20),
    member_id   VARCHAR(120)       NOT NULL UNIQUE,
    password    VARCHAR(255)       NOT NULL,
    name        VARCHAR(50)        NOT NULL,
    email       VARCHAR(255)       NOT NULL,
    birth_dt    DATETIME           NOT NULL,
    phone       VARCHAR(15)        NOT NULL,
    addr        VARCHAR(255)       NOT NULL,
    addr_detail VARCHAR(255)       NOT NULL,
    postal      VARCHAR(10)        NOT NULL,
    insert_dttm DATETIME           NOT NULL DEFAULT NOW(),
    update_dttm DATETIME           NOT NULL DEFAULT NOW(),
    state       VARCHAR(10)        NOT NULL,
    use_yn      BOOLEAN            NOT NULL DEFAULT TRUE,
    del_yn      BOOLEAN            NOT NULL DEFAULT FALSE,
    CONSTRAINT pk_member PRIMARY KEY (seq)
);
# DESC member;

# 상품관리 (내부 상품 재고 및 상태 관리)
CREATE TABLE product
(
    seq         INT AUTO_INCREMENT NOT NULL,
    product_no  VARCHAR(20)        NOT NULL UNIQUE,
    category    VARCHAR(50),
    brand       VARCHAR(100),
    name        VARCHAR(50),
    standard    VARCHAR(255),
    stock       INT,
    price       INT,
    note        VARCHAR(255),
    insert_dttm DATETIME           NOT NULL DEFAULT NOW(),
    update_dttm DATETIME           NOT NULL DEFAULT NOW(),
    state       VARCHAR(10),
    use_yn      BOOLEAN            NOT NULL DEFAULT TRUE,
    del_yn      BOOLEAN            NOT NULL DEFAULT FALSE,
    CONSTRAINT pk_product PRIMARY KEY (seq)
);
# DESC product;
# ALTER TABLE product
#     ADD COLUMN category VARCHAR(50) AFTER `product_no`;
# ALTER TABLE product
#     DROP COLUMN images;
# DROP TABLE product;
# 상품 관리 이미지
CREATE TABLE product_image
(
    seq         INT AUTO_INCREMENT NOT NULL,
    product_no  VARCHAR(20),
    name        VARCHAR(300),
    insert_dttm DATETIME           NOT NULL DEFAULT NOW(),
    update_dttm DATETIME           NOT NULL DEFAULT NOW(),
    CONSTRAINT pk_product_image PRIMARY KEY (seq),
    FOREIGN KEY (product_no) REFERENCES product (product_no)
);

# 판매상품 (상품 노출 관리)
CREATE TABLE sale
(
    seq          INT AUTO_INCREMENT NOT NULL,
    sale_no      VARCHAR(20)        NOT NULL UNIQUE,
    product_no   VARCHAR(20),
    category     VARCHAR(20),
    title        VARCHAR(255),
    quantity     INT,
    price        INT,
    delivery_fee INT,
    seq          INT AUTO_INCREMENT NOT NULL,
    sale_no      VARCHAR(20)        NOT NULL UNIQUE,
    product_no   VARCHAR(20)        NOT NULL,
    category_top VARCHAR(20)        NOT NULL,
    category_mid VARCHAR(20)        NOT NULL,
    category_sub VARCHAR(20)        NOT NULL,
    title        VARCHAR(255)       NOT NULL,
    quantity     INT                NOT NULL,
    price        INT                NOT NULL,
    delivery_fee INT                NOT NULL,
    content      VARCHAR(10000),
    view         INT,
    insert_dttm  DATETIME           NOT NULL DEFAULT NOW(),
    update_dttm  DATETIME           NOT NULL DEFAULT NOW(),
    use_yn       BOOLEAN            NOT NULL DEFAULT TRUE,
    del_yn       BOOLEAN            NOT NULL DEFAULT FALSE,
    CONSTRAINT pk_sale PRIMARY KEY (seq),
    FOREIGN KEY (product_no) REFERENCES product (product_no)
);
# DESC sale;
# ALTER TABLE sale
#     ADD COLUMN category VARCHAR(50) AFTER `product_no`;
# ALTER TABLE sale
#     DROP COLUMN category_top;

# 판매 상품 관리 썸네일 이미지
CREATE TABLE sale_image_thumb
(
    seq         INT AUTO_INCREMENT NOT NULL,
    sale_no     VARCHAR(20),
    name        VARCHAR(300),
    insert_dttm DATETIME           NOT NULL DEFAULT NOW(),
    update_dttm DATETIME           NOT NULL DEFAULT NOW(),
    CONSTRAINT pk_sale_image_thumb PRIMARY KEY (seq),
    FOREIGN KEY (sale_no) REFERENCES sale (sale_no)
);
# 판매 상품 관리 본문 이미지
CREATE TABLE sale_image_content
(
    seq         INT AUTO_INCREMENT NOT NULL,
    sale_no     VARCHAR(20)        NOT NULL,
    name        VARCHAR(300)       NOT NULL,
    insert_dttm DATETIME           NOT NULL DEFAULT NOW(),
    update_dttm DATETIME           NOT NULL DEFAULT NOW(),
    CONSTRAINT pk_sale_image_content PRIMARY KEY (seq),
    FOREIGN KEY (sale_no) REFERENCES sale (sale_no)
);

# 배송업체 관리
CREATE TABLE delivery
(
    seq         INT AUTO_INCREMENT NOT NULL,
    delivery_no VARCHAR(20)        NOT NULL UNIQUE,
    name        VARCHAR(50)        NOT NULL,
    tel         VARCHAR(15)        NOT NULL,
    insert_dttm DATETIME           NOT NULL DEFAULT NOW(),
    update_dttm DATETIME           NOT NULL DEFAULT NOW(),
    state       VARCHAR(10)        NOT NULL,
    use_yn      BOOLEAN            NOT NULL DEFAULT TRUE,
    del_yn      BOOLEAN            NOT NULL DEFAULT FALSE,
    CONSTRAINT pk_delivery PRIMARY KEY (seq)
);
# DESC delivery;

# 주문관리
CREATE TABLE order_manage
(
    seq              INT AUTO_INCREMENT NOT NULL,
#     고유번호
    order_no         VARCHAR(20)        NOT NULL UNIQUE,
#     주문번호
    sale_no          VARCHAR(20)        NOT NULL,
#     판매번호
    product_no       VARCHAR(20)        NOT NULL,
#     품번
    delivery_no      VARCHAR(20)        NOT NULL,
#     배송번호
    name             VARCHAR(50)        NOT NULL,
#     주문자 이름
    order_option     VARCHAR(255),
#     상품 옵션 정보
    count            INT                NOT NULL,
#     상품 수량
    days             INT                NOT NULL,
#     주문과 관련된 기간
    reserv_dt        DATE               NOT NULL,
#     배송 날짜
    delivery_fee     INT                NOT NULL,
#     배송비
    total_prod_price INT                NOT NULL,
#     상품 총 가격
    total_price      INT                NOT NULL,
#     최종 결제 금액
    track_no         VARCHAR(50),
#     운송장 번호
    state            VARCHAR(10)        NOT NULL,
#     주문 상태
    insert_dttm      DATETIME           NOT NULL DEFAULT NOW(),
#     주문 레코드가 등록된 날짜/시간
    update_dttm      DATETIME           NOT NULL DEFAULT NOW(),
#     주문 레코드가 마지막 수정된 날짜/시간
    CONSTRAINT pk_order_manage PRIMARY KEY (seq),
    FOREIGN KEY (sale_no) REFERENCES sale (sale_no),
    FOREIGN KEY (product_no) REFERENCES product (product_no),
    FOREIGN KEY (delivery_no) REFERENCES delivery (delivery_no)
);
# DESC order_manage;

# 반납내역
CREATE TABLE return_order
(
    seq         INT AUTO_INCREMENT NOT NULL,
    return_no   VARCHAR(20)        NOT NULL UNIQUE,
    sale_no     VARCHAR(20)        NOT NULL,
    product_no  VARCHAR(20)        NOT NULL,
    order_no    VARCHAR(20)        NOT NULL,
    addr        VARCHAR(255)       NOT NULL,
    addr_detail VARCHAR(255)       NOT NULL,
    postal      VARCHAR(10)        NOT NULL,
    name        VARCHAR(50)        NOT NULL,
    phone       VARCHAR(15)        NOT NULL,
    content     VARCHAR(255),
    state       VARCHAR(10)        NOT NULL,
    insert_dttm DATETIME           NOT NULL DEFAULT NOW(),
    update_dttm DATETIME           NOT NULL DEFAULT NOW(),
    CONSTRAINT pk_return_order PRIMARY KEY (seq),
    FOREIGN KEY (sale_no) REFERENCES sale (sale_no),
    FOREIGN KEY (product_no) REFERENCES product (product_no),
    FOREIGN KEY (order_no) REFERENCES order_manage (order_no)
);
# DESC return_order;

# 문의게시판
CREATE TABLE contact
(
    seq         INT AUTO_INCREMENT NOT NULL,
    contact_no  VARCHAR(20)        NOT NULL UNIQUE,
    member_no   VARCHAR(20)        NOT NULL,
    title       VARCHAR(255)       NOT NULL,
    name        VARCHAR(50)        NOT NULL,
    content     VARCHAR(10000)     NOT NULL,
    view        INT                NOT NULL,
    insert_dttm DATETIME           NOT NULL DEFAULT NOW(),
    update_dttm DATETIME           NOT NULL DEFAULT NOW(),
    use_yn      BOOLEAN            NOT NULL DEFAULT TRUE,
    del_yn      BOOLEAN            NOT NULL DEFAULT FALSE,
    CONSTRAINT pk_contact PRIMARY KEY (seq),
    FOREIGN KEY (member_no) REFERENCES member (member_no)
);
# DESC contact;

# 문의답변
CREATE TABLE reply
(
    seq         INT AUTO_INCREMENT NOT NULL,
    reply_no    VARCHAR(20)        NOT NULL UNIQUE,
    contact_no  VARCHAR(20)        NOT NULL,
    title       VARCHAR(255)       NOT NULL,
    name        VARCHAR(50)        NOT NULL,
    content     VARCHAR(10000)     NOT NULL,
    insert_dttm DATETIME           NOT NULL DEFAULT NOW(),
    update_dttm DATETIME           NOT NULL DEFAULT NOW(),
    use_yn      BOOLEAN            NOT NULL DEFAULT TRUE,
    del_yn      BOOLEAN            NOT NULL DEFAULT FALSE,
    CONSTRAINT pk_reply PRIMARY KEY (seq),
    FOREIGN KEY (contact_no) REFERENCES contact (contact_no)
);
# DESC reply;

# 대여관리
CREATE TABLE rental
(
    seq         INT AUTO_INCREMENT NOT NULL,
    rental_no   VARCHAR(20)        NOT NULL UNIQUE,
    order_no    VARCHAR(20)        NOT NULL,
    product_no  VARCHAR(20)        NOT NULL,
    member_no   VARCHAR(20)        NOT NULL,
    insert_dttm DATETIME           NOT NULL DEFAULT NOW(),
    update_dttm DATETIME           NOT NULL DEFAULT NOW(),
    state       VARCHAR(10)        NOT NULL,
    use_yn      BOOLEAN            NOT NULL DEFAULT TRUE,
    del_yn      BOOLEAN            NOT NULL DEFAULT FALSE,
    CONSTRAINT pk_rental PRIMARY KEY (seq),
    FOREIGN KEY (order_no) REFERENCES order_manage (order_no),
    FOREIGN KEY (product_no) REFERENCES product (product_no),
    FOREIGN KEY (member_no) REFERENCES member (member_no)
);
# DESC rental;