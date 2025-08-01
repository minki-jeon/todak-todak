import {
  Button,
  Col,
  FormControl,
  FormGroup,
  FormLabel,
  Modal,
  Row,
} from "react-bootstrap";
import { useNavigate, useParams } from "react-router";
import { useEffect, useState } from "react";
import axios from "axios";

export function ContactDetail() {
  const [contact, setContact] = useState(null);
  const [modalShow, setModalShow] = useState(false);
  const [reply, setReply] = useState("");
  const { seq } = useParams();

  let navigate = useNavigate();

  useEffect(() => {
    axios
      .get(`/api/contact/detail/${seq}`, {})
      .then((res) => {
        console.log("ok");
        const data = res.data;
        setReply(data.reply);
        setContact(data);
      })
      .catch((err) => {
        console.log("no");
      })
      .finally(() => {
        console.log("finally");
      });
  }, [seq]);

  if (!contact) return <div>로딩 중...</div>;

  function handleSaveButtonClick() {
    axios
      .put(`/api/contact/reply/${seq}`, { reply })
      .then((res) => {
        console.log("ok");
        alert(res.data.message);
        navigate(`/contact/detail/${seq}`, { replace: true });
      })
      .catch((err) => {
        console.log("no");
      })
      .finally(() => {
        console.log("finally");
      });
  }

  return (
    <>
      <Row className="justify-content-center">
        <Col xs={12} md={8} lg={6}>
          <h2
            className="mb-4"
            style={{
              // textAlign: "center",
              cursor: "pointer",
              width: "fit-content",
              transition: "color 0.2s",
              color: "#000",
            }}
            onMouseEnter={(e) => (e.target.style.color = "#007bff")}
            onMouseLeave={(e) => (e.target.style.color = "#000")}
            onClick={() => navigate("/contact/list")}
          >
            문의게시판
          </h2>
          <div>
            <FormGroup className="mb-3" controlId="title1">
              <FormLabel>제목</FormLabel>
              <FormControl value={contact.title} readOnly />
            </FormGroup>
          </div>
          <div>
            <FormGroup className="mb-3" controlId="content1">
              <FormLabel>내용</FormLabel>
              <FormControl
                readOnly
                as="textarea"
                rows={6}
                value={contact.content}
              />
            </FormGroup>
          </div>
          <div className="mb-3">
            <FormGroup>
              <FormLabel>작성자</FormLabel>
              <FormControl value={contact.name} readOnly />
            </FormGroup>
          </div>
          <Button
            variant="secondary"
            className="me-2"
            onClick={() => navigate("/contact/list")}
          >
            목록
          </Button>
          <Button
            variant="danger"
            className="me-2"
            onClick={() => setModalShow(true)}
          >
            삭제
          </Button>

          <Button
            variant="warning"
            className="me-2"
            onClick={() => navigate(`/contact/modify/${seq}`)}
          >
            수정
          </Button>

          {/*관리자 답변 */}
          <br />
          <hr />
          <div>
            <FormGroup className="mb-3" controlId="content1">
              <FormLabel className="mb-3">문의 답변 </FormLabel>
              <FormControl
                as="textarea"
                rows={6}
                value={reply}
                onChange={(e) => {
                  setReply(e.target.value);
                }}
              />
            </FormGroup>
          </div>
          <Button
            title="관리자에게만 보이게 할 예정"
            onClick={handleSaveButtonClick}
          >
            답변저장
          </Button>
        </Col>

        {/*모오오오오오오오오오오오오오오오다아아아아아아아아아아아아아아아아알ㄹㄹㄹㄹ*/}
        {/*모오오오오오오오오오오오오오오오다아아아아아아아아아아아아아아아아알ㄹㄹㄹㄹ*/}
        {/*모오오오오오오오오오오오오오오오다아아아아아아아아아아아아아아아아알ㄹㄹㄹㄹ*/}
        {/* 삭제 모달*/}
        <Modal show={modalShow} onHide={() => setModalShow(false)}>
          <Modal.Header closeButton>
            <Modal.Title>삭제 여부 확인</Modal.Title>
          </Modal.Header>
          <Modal.Body>정말 삭제하시겠습니까?</Modal.Body>
          <Modal.Footer>
            <Button variant="outline-dark" onClick={() => setModalShow(false)}>
              뒤로
            </Button>
            <Button
              variant="danger"
              onClick={() => {
                axios
                  .delete(`/api/contact/${seq}`)
                  .then((res) => {
                    console.log("ok");
                    alert(res.data.message);
                    navigate("/contact/list", { replace: true });
                  })
                  .catch((err) => {
                    console.log("no");
                  })
                  .finally(() => {
                    console.log("finally");
                  });
              }}
            >
              삭제
            </Button>
          </Modal.Footer>
        </Modal>
      </Row>
    </>
  );
}
