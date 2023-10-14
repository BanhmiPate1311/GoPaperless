import { BrowserRouter } from "react-router-dom";
import Routers from "./routers/Routers";
import { Box, CssBaseline, ThemeProvider } from "@mui/material";
import { theme } from "./ultis/theme";

function App() {
  return (
    <ThemeProvider theme={theme}>
      <CssBaseline />
      <Box className="App">
        <BrowserRouter>
          <Routers />
        </BrowserRouter>
      </Box>
    </ThemeProvider>
  );
}

export default App;
