const canvas = document.getElementById("blueprintCanvas"); // Asegúrate de que el ID sea correcto
const ctx = canvas ? canvas.getContext("2d") : null;
let points = []; // Lista de puntos en memoria

function handlePointerEvent(event) {
    if (!canvas) return; // No hacer nada si no hay canvas seleccionado

    const rect = canvas.getBoundingClientRect();
    const x = event.clientX - rect.left;
    const y = event.clientY - rect.top;

    console.log(`Click registrado en: X=${x}, Y=${y}`);

    // Agregar punto a la lista en memoria
    points.push({ x, y });

    // Repintar el canvas
    redrawCanvas();
}

function redrawCanvas() {
    if (!ctx) return; // Verificar que hay contexto de dibujo

    ctx.clearRect(0, 0, canvas.width, canvas.height); // Limpiar canvas

    ctx.fillStyle = "black"; // Color de los puntos
    points.forEach(point => {
        ctx.fillRect(point.x, point.y, 5, 5); // Dibujar cada punto
    });
}

function initCanvasHandlers() {
    if (!canvas) return;

    if (window.PointerEvent) {
        canvas.addEventListener("pointerdown", handlePointerEvent);
    } else {
        canvas.addEventListener("mousedown", handlePointerEvent);
    }
}

export { initCanvasHandlers, points };
