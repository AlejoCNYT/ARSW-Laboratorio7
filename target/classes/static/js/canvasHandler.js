const canvas = document.getElementById("blueprintCanvas"); // Asegúrate de que el ID sea correcto
const ctx = canvas.getContext("2d");
let points = []; // Lista de puntos en memoria
let currentBlueprintName = null;

// Manejo de eventos para capturar clicks en el canvas
function handlePointerEvent(event) {
    if (!currentBlueprintName) return; // No hacer nada si no hay blueprint seleccionado

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
    ctx.clearRect(0, 0, canvas.width, canvas.height); // Limpiar canvas
    ctx.beginPath();

    if (points.length > 0) {
        ctx.moveTo(points[0].x, points[0].y);
        points.forEach((point, index) => {
            if (index > 0) ctx.lineTo(point.x, point.y);
        });
    }

    ctx.stroke();
}

function initCanvasHandlers() {
    canvas.addEventListener("pointerdown", handlePointerEvent);
}

// Crear un nuevo blueprint
function createNewBlueprint() {
    // Limpiar el canvas y resetear puntos
    ctx.clearRect(0, 0, canvas.width, canvas.height);
    points = [];

    // Pedir al usuario el nombre del nuevo plano
    let blueprintName = prompt("Enter the name of the new blueprint:");
    if (!blueprintName) return;

    currentBlueprintName = blueprintName;

    // Habilitar guardado como una nueva creación
    document.getElementById("saveButton").onclick = saveNewBlueprint;
}

// 🟢 Guardar nuevo blueprint (POST + GET)
function saveNewBlueprint() {
    if (!currentBlueprintName || points.length === 0) return;

    let blueprintData = {
        name: currentBlueprintName,
        points: points
    };

    fetch("/blueprints/${currentBlueprintName}", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(blueprintData)
    })
        .then(response => response.json())
        .then(() => {
            alert("Blueprint saved successfully!");
            return fetch(`/blueprints`); // GET para actualizar la lista de planos

        })
        .then(response => response.json())
        .then(data => updateBlueprintsList(data))
        .catch(error => console.error("Error saving blueprint:", error));
}

// 🟢 Eliminar blueprint (DELETE + GET)
function deleteBlueprint() {
    console.log("Intentando borrar blueprint:", currentBlueprintName); // Debugging

    if (!currentBlueprintName) {
        alert("No blueprint selected to delete.");
        return;
    }

    const confirmed = confirm(`Are you sure you want to delete the blueprint: ${currentBlueprintName}?`);
    if (!confirmed) return;

    fetch(`/blueprints/${currentBlueprintName}`, {
        method: "DELETE",
        headers: {
            "Content-Type": "application/json"
        }
    })
        .then(response => {
            console.log("Respuesta del servidor:", response);
            if (!response.ok) {
                return response.text().then(text => { throw new Error(text); });
            }
            alert(`Blueprint "${currentBlueprintName}" deleted successfully!`);

            // 🟢 Limpiar el canvas y la lista de puntos
            ctx.clearRect(0, 0, canvas.width, canvas.height);
            points = [];

            // 🟢 Resetear variables y actualizar lista
            currentBlueprintName = null;

            // Verificar si currentAuthor está definido antes de llamar a updateBlueprintsList
            if (typeof currentAuthor !== "undefined" && currentAuthor !== null) {
                updateBlueprintsList(currentAuthor);
            } else {
                console.warn("currentAuthor no está definido, no se puede actualizar la lista.");
            }
        })
        .catch(error => console.error("Error deleting blueprint:", error));
}


// 🟢 Limpiar el canvas completamente
function clearCanvas() {
    ctx.clearRect(0, 0, canvas.width, canvas.height);
    points = [];
    currentBlueprintName = null;
}

// Actualizar lista de blueprints
function updateBlueprintsList(blueprints) {
    fetch(`/blueprints?author=${author}`)
        .then(response => response.json())
        .then(data => {
            console.log("Updated blueprints:", data);
            // Aquí puedes actualizar la UI con los nuevos planos
        })
        .catch(error => console.error("Error fetching blueprints:", error));
}

// Event listener para el botón de crear blueprint
document.getElementById("createBlueprintButton").addEventListener("click", createNewBlueprint);

export { initCanvasHandlers };
