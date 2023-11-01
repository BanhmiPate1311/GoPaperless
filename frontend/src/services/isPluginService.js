import { api } from "../constants/api";

export const isPluginService = {
  getHash: (data) => {
    return api.post("/is/getHashFileFps", data);
  },
  signUsbTokenFps: (data) => {
    return api.post("/is/signUsbTokenFps", data);
  },
};
