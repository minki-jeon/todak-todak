#문의 게시판
CREATE TABLE contact
(
    seq        INT AUTO_INCREMENT NOT NULL,
    contact_no VARCHAR(255)       NULL,
    CONSTRAINT pk_contact PRIMARY KEY (seq)
);

DROP TABLE contact;

#임시 문의게시판
CREATE TABLE contact
(
    seq         INT            NOT NULL,
    contact_no  VARCHAR(20)    NOT NULL UNIQUE,
    #           member_no VARCHAR(20)    NOT NULL,
    title       VARCHAR(255)   NOT NULL,
    name        VARCHAR(50)    NOT NULL,
    content     VARCHAR(10000) NOT NULL,
    view        INT            NOT NULL DEFAULT 0,
    insert_dttm DATETIME       NOT NULL DEFAULT NOW(),
    update_dttm DATETIME       NOT NULL DEFAULT NOW(),
    use_yn      BOOLEAN        NOT NULL DEFAULT TRUE,
    del_yn      BOOLEAN        NOT NULL DEFAULT FALSE,
    CONSTRAINT pk_contact PRIMARY KEY (seq)
);
/*
# 게시판 트리거 걸기 자동 값 생성
DELIMITER
$$

CREATE TRIGGER before_contact_insert
    BEFORE INSERT
    ON contact
    FOR EACH ROW
BEGIN
    -- contact_no 값 설정 (CO + 날짜 + 7자리 seq)
    SET NEW.contact_no = CONCAT(
            'CO',
            DATE_FORMAT(NOW(), '%y%m%d'), -- 현재 날짜 (yyMMdd)
            LPAD(NEW.seq, 7, '0') -- seq 값(7자리로 padding)
                         );
END $$

DELIMITER
*/


# 게시판 드랍
DROP TABLE contact;

# 게시판 확인
SELECT *
FROM contact;

#임시게시판 (사용x)
CREATE TABLE contact
(
    seq         INT            NOT NULL,
    title       VARCHAR(255)   NOT NULL,
    name        VARCHAR(50)    NOT NULL,
    content     VARCHAR(10000) NOT NULL,
    view        INT            NOT NULL DEFAULT 0,
    insert_dttm DATETIME       NOT NULL DEFAULT NOW(),
    update_dttm DATETIME       NOT NULL DEFAULT NOW(),
    use_yn      BOOLEAN        NOT NULL DEFAULT TRUE,
    del_yn      BOOLEAN        NOT NULL DEFAULT FALSE,
    CONSTRAINT pk_contact PRIMARY KEY (seq)
);


# 임시임시 게시판
CREATE TABLE contact
(
    seq         INT             NOT NULL AUTO_INCREMENT,
    contact_no  VARCHAR(20)     NOT NULL UNIQUE,
    title       VARCHAR(255)    NOT NULL,
    name        VARCHAR(50)     NOT NULL,
    content     VARCHAR(10000)  NOT NULL,
    view        INT                      DEFAULT 0,
    insert_dttm DATETIME        NOT NULL DEFAULT NOW(),
    update_dttm DATETIME        NOT NULL DEFAULT NOW(),
    reply       VARBINARY(1000) NOT NULL,
    reply_dttm  DATETIME        NOT NULL DEFAULT NOW(),
    use_yn      BOOLEAN         NOT NULL DEFAULT TRUE,
    del_yn      BOOLEAN         NOT NULL DEFAULT FALSE,
    CONSTRAINT pk_contact PRIMARY KEY (seq)
);

DROP TABLE contact;


# 주문 배송 정보
CREATE TABLE order_info
(
    seq          INT          NOT NUll AUTO_INCREMENT,
    order_no     VARCHAR(20)  NOT NULL UNIQUE,
    sale_no      VARCHAR(20)  NOT NULL,
#     member_no   VARCHAR(20)  NOT NULL,
    name         VARCHAR(50)  NOT NULL,
    phone_no     VARCHAR(30)  NOT NULL,
    post_code    VARCHAR(10)  NOT NULL,
    addr         VARCHAR(255) NOT NULL,
    addr_detail  VARCHAR(255) NOT NULL,
    request      VARCHAR(255) NOT NULL,
    price        INT          NOT NULL,
    delivery_fee INT          NOT NULL,
    order_count  INT          NOT NULL,
    Constraint pk_order_info PRIMARY KEY (seq),
    FOREIGN KEY (sale_no) REFERENCES sale (sale_no)
);

DROP TABLE order_info;


# 새로만든 문의게시판 멤버와 추후연결
# 댓글 내용 추가 필요
/*CREATE TABLE contact
(
    seq         INT            NOT NULL AUTO_INCREMENT,
    contact_no  VARCHAR(20)    NOT NULL UNIQUE,
    member_no   VARCHAR(20)    NOT NULL,
    title       VARCHAR(255)   NOT NULL,
    name        VARCHAR(50)    NOT NULL,
    content     VARCHAR(10000) NOT NULL,
    view        INT            NOT NULL DEFAULT 0,
    insert_dttm DATETIME       NOT NULL DEFAULT NOW(),
    update_dttm DATETIME       NOT NULL DEFAULT NOW(),
    reply       VARBINARY(1000) NOT NULL,
    reply_dttm  DATETIME        NOT NULL DEFAULT NOW(),
    use_yn      BOOLEAN        NOT NULL DEFAULT TRUE,
    del_yn      BOOLEAN        NOT NULL DEFAULT FALSE,
    CONSTRAINT pk_contact PRIMARY KEY (seq),
    FOREIGN KEY (member_no) REFERENCES member (member_no)
);*/

