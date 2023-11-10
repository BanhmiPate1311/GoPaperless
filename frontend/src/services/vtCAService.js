import { api } from "../constants/api";

export const vtCAService = {
  getCertificate: (data) => {
    return api.post("/viettel-ca/getCertificate", data);
  },
  signHash: (data, signal) => {
    return api.post("/viettel-ca/signHash", data, { signal });
  },
};
