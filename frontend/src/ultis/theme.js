import { createTheme, responsiveFontSizes } from "@mui/material/styles";
import { red } from "@mui/material/colors";

// Create a theme instance.
export let theme = createTheme({
  typography: {
    fontFamily: "Montserrat,Nucleo,Helvetica,sans-serif",
    h6: {
      fontSize: 14, // Adjust the font size as needed
    },
    h5: {
      fontSize: 13, // Adjust the font size as needed
    },
  },
  palette: {
    primary: {
      main: "#26293F",
    },
    secondary: {
      light: "#EDF7FA",
      main: "#00A8CC",
    },
    error: {
      main: red.A400,
    },
    text: {
      primary: "#21243D",
    },
  },
  components: {
    MuiContainer: {
      defaultProps: {
        maxWidth: "md",
      },
      styleOverrides: {
        maxWidthSm: {
          maxWidth: "680px",

          "@media (min-width: 600px)": {
            maxWidth: "680px",
          },
        },
        maxWidthMd: {
          maxWidth: "860px",

          "@media (min-width: 900px)": {
            maxWidth: "860px",
          },
        },
      },
    },
    MuiLink: {
      defaultProps: {
        underline: "none",
      },
      styleOverrides: {
        root: {
          color: "black",

          "&:hover, &.active": {
            color: "#FF6464",
          },
        },
      },
    },
    MuiButton: {
      styleOverrides: {
        root: {
          // background: '#df0f0f', // set cho tất cả MuiButton
          // color: 'white',
          textTransform: "capitalize",
        },
      },
      variants: [
        {
          props: { variant: "contained", color: "primary" },
          style: {
            color: "white",
          },
        },
      ],
    },
    MuiChip: {
      styleOverrides: {
        root: {
          paddingInline: 2, // set cho tất cả MuiChip
        },
      },
      variants: [
        {
          props: { color: "secondary" },
          style: {
            color: "white",
            backgroundColor: "#142850", // chỉ set cho MuiChip có prop secondary
            fontSize: 16,
            fontWeight: "bold",
          },
        },
      ],
    },
  },
});

theme = responsiveFontSizes(theme);

// theme.typography.h3 = {
// 	fontSize: '2rem',

// 	[theme.breakpoints.up('md')]: {
// 		fontSize: '3rem',
// 	},
// }
