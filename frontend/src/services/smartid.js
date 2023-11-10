import { api } from "../constants/api";

export const smartIdService = {
  getCertificate: (data) => {
    return api.post("/getCertificate", data);
  },
  sign: (data, signal) => {
    return api.post("/signFile", data, { signal });
  },
};
