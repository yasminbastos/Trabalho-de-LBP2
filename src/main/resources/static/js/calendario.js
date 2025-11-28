const calendar = document.querySelector(".calendar"),
  date = document.querySelector(".date"),
  daysContainer = document.querySelector(".days"),
  prev = document.querySelector(".prev"),
  next = document.querySelector(".next"),
  todayBtn = document.querySelector(".today-btn"),
  gotoBtn = document.querySelector(".goto-btn"),
  dateInput = document.querySelector(".date-input"),
  eventDay = document.querySelector(".event-day"),
  eventDate = document.querySelector(".event-date"),
  eventsContainer = document.querySelector(".events"),
  addEventBtn = document.querySelector(".add-event");

let today = new Date();
let activeDay;
let month = today.getMonth();
let year = today.getFullYear();

const humorEventsArr = window.backendHumorEvents ? [...window.backendHumorEvents] : [];
const eventsArr      = window.backendNoteEvents  ? [...window.backendNoteEvents]  : [];
console.log("Humores:", humorEventsArr);
console.log("Notas:", eventsArr);

const moodColors = {
  "Calmo": "#9c27b0",
  "Feliz": "#4caf50",
  "Triste": "#2196f3",
  "Bravo": "#f44336",
  "Desapontado": "#607d8b",
  "Preocupado": "#ffb300",
  "Assustado": "#3f51b5",
  "Frustrado": "#e91e63",
  "Estressado": "#ff5722",
  "Enjoado": "#4db6ac",
  "Pensativo": "#8d6e63",
  "Animado": "#ff9800",
  "Envergonhado": "#ff6f91",
  "Tedioso": "#9e9e9e"
};

const months = [
  "Janeiro",
  "Fevereiro",
  "Maro",
  "Abril",
  "Maio",
  "Junho",
  "Julho",
  "Agosto",
  "Setembro",
  "Outubro",
  "Novembro",
  "Dezembro",
];

function initCalendar() {
  const firstDay = new Date(year, month, 1);
  const lastDay = new Date(year, month + 1, 0);
  const prevLastDay = new Date(year, month, 0);
  const prevDays = prevLastDay.getDate();
  const lastDate = lastDay.getDate();
  const day = firstDay.getDay();
  const nextDays = 7 - lastDay.getDay() - 1;

  date.innerHTML = months[month] + " " + year;

  let days = "";

  for (let x = day; x > 0; x--) {
    days += `<div class="day prev-date">${prevDays - x + 1}</div>`;
  }

  for (let i = 1; i <= lastDate; i++) {
    let event = false;
    eventsArr.forEach((eventObj) => {
      if (
        eventObj.day === i &&
        eventObj.month === month + 1 &&
        eventObj.year === year
      ) {
        event = true;
      }
    });
    if (
      i === new Date().getDate() &&
      year === new Date().getFullYear() &&
      month === new Date().getMonth()
    ) {
      activeDay = i;
      getActiveDay(i);
      updateEvents(i);
      if (event) {
        days += `<div class="day today active event">${i}</div>`;
      } else {
        days += `<div class="day today active">${i}</div>`;
      }
    } else {
      if (event) {
        days += `<div class="day event">${i}</div>`;
      } else {
        days += `<div class="day ">${i}</div>`;
      }
    }
  }

  for (let j = 1; j <= nextDays; j++) {
    days += `<div class="day next-date">${j}</div>`;
  }
  daysContainer.innerHTML = days;

  const dayElements = daysContainer.querySelectorAll(".day");
  dayElements.forEach((dayEl) => {
    // não pinta humor em dias de outro mês
    if (dayEl.classList.contains("prev-date") || dayEl.classList.contains("next-date")) {
      return;
    }

    const dayNumber = Number(dayEl.textContent);
    const dayObj = humorEventsArr.find(
      (e) => e.day === dayNumber && e.month === month + 1 && e.year === year
    );
    if (dayObj && dayObj.events && dayObj.events.length > 0) {
      const mood = dayObj.events[0].title;
      const color = moodColors[mood];
      if (color) {
        dayEl.style.setProperty("--event-color", color);
        dayEl.classList.add("event");
      }
    }
  });

  addListner();
}

function prevMonth() {
  month--;
  if (month < 0) {
    month = 11;
    year--;
  }
  initCalendar();
}

function nextMonth() {
  month++;
  if (month > 11) {
    month = 0;
    year++;
  }
  initCalendar();
}

prev.addEventListener("click", prevMonth);
next.addEventListener("click", nextMonth);

initCalendar();

function addListner() {
  const days = document.querySelectorAll(".day");
  days.forEach((day) => {
    day.addEventListener("click", (e) => {
      const value = Number(e.target.innerHTML);
      activeDay = value;
      getActiveDay(value);
      updateEvents(value);

      days.forEach((d) => d.classList.remove("active"));

      if (e.target.classList.contains("prev-date")) {
        prevMonth();
        setTimeout(() => {
          const days2 = document.querySelectorAll(".day");
          days2.forEach((d) => {
            if (
              !d.classList.contains("prev-date") &&
              Number(d.innerHTML) === value
            ) {
              d.classList.add("active");
            }
          });
        }, 100);
      } else if (e.target.classList.contains("next-date")) {
        nextMonth();
        setTimeout(() => {
          const days2 = document.querySelectorAll(".day");
          days2.forEach((d) => {
            if (
              !d.classList.contains("next-date") &&
              Number(d.innerHTML) === value
            ) {
              d.classList.add("active");
            }
          });
        }, 100);
      } else {
        e.target.classList.add("active");
      }
    });
  });
}

todayBtn.addEventListener("click", () => {
  today = new Date();
  month = today.getMonth();
  year = today.getFullYear();
  initCalendar();
});

dateInput.addEventListener("input", (e) => {
  dateInput.value = dateInput.value.replace(/[^0-9/]/g, "");
  if (dateInput.value.length === 2) {
    dateInput.value += "/";
  }
  if (dateInput.value.length > 7) {
    dateInput.value = dateInput.value.slice(0, 7);
  }
  if (e.inputType === "deleteContentBackward") {
    if (dateInput.value.length === 3) {
      dateInput.value = dateInput.value.slice(0, 2);
    }
  }
});

gotoBtn.addEventListener("click", gotoDate);

function gotoDate() {
  const dateArr = dateInput.value.split("/");
  if (dateArr.length === 2) {
    if (dateArr[0] > 0 && dateArr[0] < 13 && dateArr[1].length === 4) {
      month = dateArr[0] - 1;
      year = dateArr[1];
      initCalendar();
      return;
    }
  }
  alert("Data Inválida");
}

function getActiveDay(dateNumber) {
  const day = new Date(year, month, dateNumber);
  const dayName = day.toString().split(" ")[0];
  eventDay.innerHTML = dayName;
  eventDate.innerHTML = dateNumber + " " + months[month] + " " + year;
}

function updateEvents(dateNumber) {
  let events = "";

  // notas
  eventsArr.forEach((event) => {
    if (
      dateNumber === event.day &&
      month + 1 === event.month &&
      year === event.year
    ) {
      event.events.forEach((ev) => {
        events += `<div class="event" data-id="${event.id}">
            <div class="title">
              <i class="fas fa-circle"></i>
              <h3 class="event-title">${ev.title}</h3>
            </div>
            <div class="event-time">
              <span class="event-time">${ev.time}</span>
            </div>
        </div>`;
      });
    }
  });

  // humor
  humorEventsArr.forEach((event) => {
    if (
      dateNumber === event.day &&
      month + 1 === event.month &&
      year === event.year
    ) {
      event.events.forEach((ev) => {
        events += `<div class="event event-humor">
            <div class="title">
              <i class="fas fa-circle"></i>
              <h3 class="event-title">${ev.title}</h3>
            </div>
            <div class="event-time">
              <span class="event-time">${ev.time || ""}</span>
            </div>
        </div>`;
      });
    }
  });

  if (events === "") {
    events = `<div class="no-event">
            <h3>Nenhuma Nota</h3>
        </div>`;
  }
  eventsContainer.innerHTML = events;
}

// botão de adicionar nota (vai para /notas com a data clicada)
addEventBtn.addEventListener("click", () => {
  if (!activeDay) {
    alert("Selecione um dia no calendário primeiro.");
    return;
  }
  const selectedDay = activeDay;
  const selectedMonth = month + 1; // 1-12
  const selectedYear = year;
  window.location.href =
    `/notas?day=${selectedDay}&month=${selectedMonth}&year=${selectedYear}`;
});

// popup de exclusão
const popup = document.createElement("div");
popup.classList.add("popup");
popup.innerHTML = `
  <div class="popup-content">
    <p>Tem certeza de que deseja excluir esta nota?</p>
    <div class="popup-buttons">
      <button id="confirmDelete" class="confirm-btn">Sim</button>
      <button id="cancelDelete" class="cancel-btn">Cancelar</button>
    </div>
  </div>
`;
document.body.appendChild(popup);

function openPopup() { popup.classList.add("active"); }
function closePopup() { popup.classList.remove("active"); }

let selectedEventId = null;

eventsContainer.addEventListener("click", (e) => {
  const eventEl = e.target.closest(".event");
  if (!eventEl) return;
  if (eventEl.classList.contains("event-humor")) return;

  selectedEventId = eventEl.getAttribute("data-id");
  if (!selectedEventId) return;

  openPopup();
});

document.getElementById("confirmDelete").addEventListener("click", async () => {
  if (!selectedEventId) return;

  const resp = await fetch(`/notas/${selectedEventId}/excluir`, {
    method: "POST",
    headers: {
      "Content-Type": "application/x-www-form-urlencoded;charset=UTF-8",
    },
  });

  if (resp.ok) {
    window.location.href = "/calendario";
  } else {
    alert("Erro ao excluir nota.");
  }
  closePopup();
  selectedEventId = null;
});


document.getElementById("cancelDelete").addEventListener("click", () => {
  closePopup();
  selectedEventId = null;
});

function convertTime(time) {
  let timeArr = time.split(":");
  let timeHour = timeArr[0];
  let timeMin = timeArr[1];
  let timeFormat = timeHour >= 12 ? "PM" : "AM";
  timeHour = timeHour % 12 || 12;
  time = timeHour + ":" + timeMin + " " + timeFormat;
  return time;
}

const menuIcon = document.getElementById("menu-icon");
const menu = document.getElementById("menu");

if (menuIcon && menu) {
  menuIcon.addEventListener("click", () => {
    menu.style.display = menu.style.display === "block" ? "none" : "block";
  });
}
