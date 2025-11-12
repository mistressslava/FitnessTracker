import axios from "axios";

axios.defaults.withCredentials = true;

if (window.location.host === "localhost:5173") {
    axios.defaults.baseURL = "http://localhost:8080";
} else {
    axios.defaults.baseURL = window.location.origin;
}
