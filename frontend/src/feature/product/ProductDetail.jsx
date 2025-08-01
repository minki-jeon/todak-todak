import { useNavigate, useParams } from "react-router";
import { useEffect, useState } from "react";
import axios from "axios";
import { toast } from "react-toastify";
import {
  Button,
  Col,
  FormControl,
  FormGroup,
  FormLabel,
  Image,
  ListGroup,
  ListGroupItem,
  Modal,
  Row,
  Spinner,
} from "react-bootstrap";

export function ProductDetail() {
  const [product, setProduct] = useState(null);
  const { seq } = useParams();
  const navigate = useNavigate();
  const [modalShow, setModalShow] = useState(false);

  useEffect(() => {
    axios
      .get(`/api/product/detail/${seq}`)
      .then((res) => {
        setProduct(res.data);
      })
      .catch((err) => {
        toast("해당 상품이 존재하지 않습니다.", { type: "warning" });
      })
      .finally(() => {
        console.log("항상 실행");
      });
  }, []);

  if (!product) {
    return <Spinner />;
  }

  function handleDeleteButtonClick() {
    axios
      .put(`/api/product/${seq}`)
      .then((res) => {
        const message = res.data.message;
        if (message) {
          toast(message.text, { type: message.type });
        }
        navigate("/product/list");
      })
      .catch((err) => {
        console.log("동작 오류");
        toast("상품이 삭제되지 않았습니다.", { type: "warning" });
      })
      .finally(() => {
        console.log("항상 실행");
      });
  }

  function handleMoveSaleAddButtonClick() {
    // 카테고리명, 상품번호
    const searchParams = new URLSearchParams();
    searchParams.set("categoryName", product.category);
    searchParams.set("productNo", product.productNo);
    navigate(`/sale/add?${searchParams.toString()}`);
  }

  return (
    <Row className="justify-content-center">
      <Col xs={12} md={8} lg={6}>
        <div className="d-flex justify-content-between">
          <h2 className="mb-4">{product.seq} 번 상품</h2>
        </div>
        <div>
          <Button variant="primary" onClick={handleMoveSaleAddButtonClick}>
            판매 등록
          </Button>
        </div>
        <div>
          <FormGroup className="mb-3" controlId="formName">
            <FormLabel>상품명</FormLabel>
            <FormControl value={product.name} readOnly={true} />
          </FormGroup>
        </div>
        <div>
          <FormGroup className="mb-3" controlId="formCategory">
            <FormLabel>카테고리</FormLabel>
            <FormControl value={product.category} readOnly={true} />
          </FormGroup>
        </div>
        <div>
          <FormGroup className="mb-3" controlId="formBrand">
            <FormLabel>브랜드명</FormLabel>
            <FormControl value={product.brand} readOnly={true} />
          </FormGroup>
        </div>
        <div>
          <FormGroup className="mb-3" controlId="formStandard">
            <FormLabel>규격</FormLabel>
            <FormControl value={product.standard} readOnly={true} />
          </FormGroup>
        </div>
        <div>
          <FormGroup className="mb-3" controlId="formStock">
            <FormLabel>재고량</FormLabel>
            <FormControl value={product.stock} readOnly={true} />
          </FormGroup>
        </div>
        <div>
          <FormGroup className="mb-3" controlId="formPrice">
            <FormLabel>단위가격</FormLabel>
            <FormControl value={product.price} readOnly={true} />
          </FormGroup>
        </div>
        <div>
          <FormGroup className="mb-3" controlId="formNote">
            <FormLabel>비고</FormLabel>
            <FormControl value={product.note} readOnly={true} />
          </FormGroup>
        </div>
        <div>
          <FormGroup className="mb-3" controlId="formInsertDttm">
            <FormLabel>등록일시</FormLabel>
            <FormControl
              value={product.insertDttm}
              type="datetime-local"
              readOnly={true}
            />
          </FormGroup>
        </div>
        <div>
          <FormGroup className="mb-3" controlId="formUpdateDttm">
            <FormLabel>수정일시</FormLabel>
            <FormControl
              value={product.updateDttm}
              type="datetime-local"
              readOnly={true}
            />
          </FormGroup>
        </div>
        <div>
          <FormGroup className="mb-3" controlId="formState">
            <FormLabel>상품등록상태</FormLabel>
            <FormControl
              value={product.state || "상태값 없음"}
              readOnly={true}
            />
          </FormGroup>
        </div>
        <div>
          <FormGroup className="mb-3" controlId="formUseYn">
            <FormLabel>상품등록여부</FormLabel>
            <FormControl value={product.useYn} readOnly={true} />
          </FormGroup>
        </div>

        <div className="mb-3">
          상품 이미지
          <ListGroup>
            {product.images.map((image) => (
              <ListGroupItem key={image.name}>
                <Image fluid src={image.path} />
              </ListGroupItem>
            ))}
          </ListGroup>
        </div>

        <div>
          <Button
            className="me-2"
            variant="outline-danger"
            onClick={() => setModalShow(true)}
          >
            삭제
          </Button>
          <Button
            variant="outline-info"
            onClick={() => navigate(`/product/modify/${product.seq}`)}
          >
            수정
          </Button>
        </div>
      </Col>

      <Modal show={modalShow} onHide={() => setModalShow(false)}>
        <Modal.Header closeButton>
          <Modal.Title>상품 삭제 확인</Modal.Title>
        </Modal.Header>
        <Modal.Body>{product.seq} 번 상품을 삭제하시겠습니까?</Modal.Body>
        <Modal.Footer>
          <Button variant="outline-dark" onClick={() => setModalShow(false)}>
            취소
          </Button>
          <Button variant="danger" onClick={handleDeleteButtonClick}>
            삭제
          </Button>
        </Modal.Footer>
      </Modal>
    </Row>
  );
}
