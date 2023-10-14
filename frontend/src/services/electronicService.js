import { api } from "../constants/api";

export const electronicService = {
  checkIdentity: (data) => {
    return api.post("/elec/checkPersonalCode", data);
  },
  faceAndCreate: (data) => {
    return api.post("/elec/faceAndCreate", data);
  },
  updateSubject: (data) => {
    return api.post("/elec/updateSubject", data);
  },
  perFormProcess: (data) => {
    return api.post("/elec/processPerForm", data);
  },
  processOTPResend: (data) => {
    return api.post("/elec/processOTPResend", data);
  },
  checkCertificate: (data) => {
    return api.post("/elec/checkCertificate", data);
  },
  createCertificate: (data) => {
    return api.post("/elec/createCertificate", data);
  },
  credentialOTP: (data) => {
    return api.post("/elec/credentialOTP", data);
  },
  authorizeOTP: (data) => {
    return api.post("/elec/authorizeOTP", data);
  },
};

//mẫu
// export const electronicService = {
//     checkIdentity: (data) => {
//       const formData = new FormData();

//       // Đặt các trường dữ liệu trong FormData
//       formData.append('field1', data.field1); // Thay 'field1' và data.field1 bằng tên và giá trị thực tế của bạn
//       formData.append('field2', data.field2);
//       // Thêm các trường dữ liệu khác nếu cần

//       // Tạo yêu cầu Axios với "multipart/form-data" Content-Type
//       return axios.post("/elec/checkPersonalCode", formData, {
//         headers: {
//           'Content-Type': 'multipart/form-data',
//         },
//       });
//     },
//   };
