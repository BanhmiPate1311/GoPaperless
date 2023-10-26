import { api } from "../constants/api";

export const fpsService = {
  getDocumentDetails: ({ documentId }) => {
    return api.get(`/fps/${documentId}/getDocumentDetails`);
  },
  getFields: ({ documentId }) => {
    return api.get(`/fps/${documentId}/getFields`);
  },
  getSignatureAfterVerify: ({ documentId }) => {
    return api.get(`/fps/${documentId}/verification`);
  },
  getImage: ({ documentId, page }) => {
    return api.get(`/fps/${documentId}/${page}/images`, {
      responseType: "blob",
    });
  },
  getVerification: ({ documentId }) => {
    return api.get(`/fps/${documentId}/verification`);
  },
  addSignature: async ({ document_id }, data, { field }) => {
    try {
      const response = await api.post(
        `/fps/${document_id}/${field}/addSignature`,
        data
      );
      return response.data; // Trả về dữ liệu từ câu trả lời của máy chủ
    } catch (error) {
      console.error("Lỗi trong quá trình gửi yêu cầu:", error);
      throw error; // Ném lỗi để xử lý ở nơi gọi hàm này
    }
  },
  putSignature: async ({ document_id }, data, { field }) => {
    try {
      const response = await api.put(
        `/fps/${document_id}/${field}/putSignature`,
        data
      );
      return response.data; // Trả về dữ liệu từ câu trả lời của máy chủ
    } catch (error) {
      console.error("Lỗi trong quá trình gửi yêu cầu:", error);
      throw error; // Ném lỗi để xử lý ở nơi gọi hàm này
    }
  },
  removeSignature: async ({ document_id }, field_name) => {
    try {
      const response = await api.delete(
        `/fps/${document_id}/${field_name}/deleteSignatue`
      );
    } catch (error) {
      console.error("Lỗi trong quá trình gửi yêu cầu:", error);
      throw error; // Ném lỗi để xử lý ở nơi gọi hàm này
    }
  },
};
