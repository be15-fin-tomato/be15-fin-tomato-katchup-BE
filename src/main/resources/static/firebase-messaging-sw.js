// firebase-messaging-sw.js
importScripts("https://www.gstatic.com/firebasejs/10.12.0/firebase-app-compat.js");
importScripts("https://www.gstatic.com/firebasejs/10.12.0/firebase-messaging-compat.js");

firebase.initializeApp({
    apiKey: "AIzaSyBOnX8kKdcvIdtdwJ2O4-mbQwxaQGuZtwA",
    projectId: "tomato-katchup",
    messagingSenderId: "101664121020",
    appId: "1:101664121020:web:525beb263a7bbdbc7530b9"
});

const messaging = firebase.messaging();
