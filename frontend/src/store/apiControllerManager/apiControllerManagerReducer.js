import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import { api } from "../../constants/api";

const initialState = {
  isSignSuccess: "",
  messageError: "",
  headerFooter: [],
  pdfFile: {},
  signingOptions: [],
  codeVC: "",
  isFetching: false,
  errorSignFile: undefined,
  isLoading: false,
  signaturePrepare: [],
};

export const {
  reducer: apiControllerManagerReducer,
  actions: apiControllerManagerActions,
} = createSlice({
  name: "apiControllerManager",
  initialState,
  // Xử lý action đồng bộ
  reducers: {
    setMessageSuccess(state) {
      state.isSignSuccess = "Document signed successfully.";
    },
    clearsetMessageSuccess(state) {
      state.isSignSuccess = "";
    },
    setMessageError(state, action) {
      state.messageError = action.payload;
    },
    clearsetMessageError(state) {
      state.messageError = "";
    },
    setIsLoading(state, action) {
      state.isLoading = action.payload;
    },
    setSignaturePrepare(state, action) {
      if (action.payload) {
        const index = state.signaturePrepare.findIndex(
          (item) =>
            item.workFlowId === action.payload.workFlowId &&
            item.field_name === action.payload.field_name
        );
        if (index === -1) {
          state.signaturePrepare.push(action.payload);
        } else {
          state.signaturePrepare[index] = action.payload;
        }
      }
    },
  },

  // Xử lý action bất đồng bộ
  extraReducers: (builder) => {
    builder
      //getHeaderFooter
      .addCase(getHeaderFooter.pending, (state, action) => {
        // state.isFetching = true;
      })
      .addCase(getHeaderFooter.fulfilled, (state, action) => {
        state.headerFooter = action.payload;
        // state.isFetching = false;
      })
      .addCase(getHeaderFooter.rejected, (state, action) => {
        // state.error = action.payload;
        // state.isFetching = false;
      })

      //getHeaderFooterBatch
      .addCase(getHeaderFooterBatch.pending, (state, action) => {
        // state.isFetching = true;
      })
      .addCase(getHeaderFooterBatch.fulfilled, (state, action) => {
        state.headerFooter = action.payload;
        // state.isFetching = false;
      })
      .addCase(getHeaderFooterBatch.rejected, (state, action) => {
        // state.error = action.payload;
        // state.isFetching = false;
      })

      //showFile
      .addCase(showFile.pending, (state, action) => {
        // state.isFetching = true;
      })
      .addCase(showFile.fulfilled, (state, action) => {
        // state.isFetching = false;
        state.pdfFile = action.payload;
      })
      .addCase(showFile.rejected, (state, action) => {
        // state.isFetching = false;
        // state.error = action.payload;
      })

      // getSigningOption
      .addCase(getSigningOption.pending, (state, action) => {
        // state.isFetching = true;
      })
      .addCase(getSigningOption.fulfilled, (state, action) => {
        // state.isFetching = false;
        state.signingOptions =
          action.payload !== null
            ? action.payload.split(",")
            : [
                "mobile",
                "smartid",
                "usbtoken",
                "eidsigncloud",
                "eidwitnessing",
              ];
      })
      .addCase(getSigningOption.rejected, (state, action) => {
        // state.isFetching = false;
        // state.error = action.payload;
      });
  },
});

export const getHeaderFooter = createAsyncThunk(
  "apiControllerManager/getHeaderFooter",
  async (data, { dispatch, getState, rejectWithValue }) => {
    try {
      const response = await api.post("/headerfooter", {
        signingToken: data,
      });
      console.log("response: ", response.data);
      return response.data;
    } catch (error) {
      console.log("error: ", error);
      return rejectWithValue(error.response.data);
    }
  }
);

export const getHeaderFooterBatch = createAsyncThunk(
  "apiControllerManager/getHeaderFooterBatch",
  async (data, { dispatch, getState, rejectWithValue }) => {
    try {
      const response = await api.post("/headerfooterBatch", {
        batch_token: data,
      });
      console.log("response: ", response.data);
      return response.data;
    } catch (error) {
      console.log("error: ", error);
      return rejectWithValue(error.response.data);
    }
  }
);

export const showFile = createAsyncThunk(
  "apiControllerManager/showFile",
  async (data, { dispatch, getState, rejectWithValue }) => {
    try {
      const response = await api.post("/showFile", { signingToken: data });
      // console.log("showFile: ", response.data);
      return response.data;
    } catch (error) {
      console.log("error: ", error);
      return rejectWithValue(error.response.data);
    }
  }
);

export const getSigningOption = createAsyncThunk(
  "apiControllerManager/getSigningOption",
  async (data, { dispatch, getState, rejectWithValue }) => {
    try {
      const response = await api.post("/getSigningOption", {
        signerToken: data,
      });
      // console.log("SigningOption2: ", response.data[0]);
      return response.data[0];
    } catch (error) {
      console.log("error: ", error);
      return rejectWithValue(error.response.data);
    }
  }
);
