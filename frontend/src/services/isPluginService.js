import { api } from "../constants/api";

export const isPluginService = {
  getHash: (data) => {
    return api.get("/is/getHashFile2", data);
  },
  signUsbTokenFps: (data) => {
    return api.get("/is/signUsbTokenFps", data);
  },
};
