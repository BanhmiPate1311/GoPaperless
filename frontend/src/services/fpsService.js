import { AxiosFPS, api } from "../constants/api";
const bear =
  "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2OTcxOTE4NjIyMzQsImlhdCI6MTY5NzE4ODI2MjIzNCwiaXNzIjoiaHR0cHM6Ly9mcHMubW9iaWxlLWlkLnZuIiwiYXVkIjoiZW50ZXJwcmlzZSIsInN1YiI6IkZQUyIsInR5cCI6IkJlYXJlciIsInNpZCI6IjQ5MDctMzY0MjUtNDk4NDIiLCJhenAiOiJNb2JpbGUtSUQgQ29tcGFueSIsIm1vYmlsZSI6IjE5MDAgNjg4NCIsImFpZCI6M30.cYSWB3vNyiuwLhZNy0iJX3Z-itk-eVUP4RpFso6_VRcr4M7q3GE6zOkA4UYruRu0BnTJv2YPGvYXYRP0Y7Y-YmIHAPFxBL9ZnTBKXSlLsVY2amaXwRy50O9MHbVSA3rEqvZzhjF6lEkU6f4EslwonJv8z0bYItHRhAIqJepkfqJxJyiEuc_G6Lj-PiOf7J8jyJgl9i3SWrP9t6QJbl-mWm-A1uG4C5BUA6M2cKMR1sAJyiVckP7yabbCoVjWXmXXoc9IWtGuFQzW8Zj9MTcE-jySyBwD7ebsETScDAxFaGeaIV3SKHz9PMPWwbWOwYOHwS1VYHXvEWHzwh8y8bzp3w";

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
        `/fps/v1/documents/${document_id}/fields/${field}`,
        data
      );
      return response.data; // Trả về dữ liệu từ câu trả lời của máy chủ
    } catch (error) {
      console.error("Lỗi trong quá trình gửi yêu cầu:", error);
      throw error; // Ném lỗi để xử lý ở nơi gọi hàm này
    }
  },
  // removeSignature: async ({ document_id }, field_name) => {
  //   AxiosFPS.delete(`/fps/v1/documents/${document_id}/fields`, {
  //     headers: {
  //       "Content-Type": "application/json",
  //       Authorization: `Bearer ${bear}`,
  //     },
  //     data: {
  //       field_name,
  //     },
  //   }).catch((err) => {});
  // },
};
