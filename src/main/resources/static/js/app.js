import { initCanvasHandlers } from "./canvasHandler.js";

var App = (function () {
    let api = apimock;
    let currentAuthor = "";
    let currentBlueprintName = null;
    let blueprintActual = null;

    function saveOrUpdateBlueprint() {
        if (!blueprintActual || !currentAuthor) {
            alert("No hay un plano seleccionado para actualizar.");
            return;
        }

        console.log("⏳ Guardando Blueprint...", blueprintActual);

        fetch(`/blueprints/${encodeURIComponent(currentAuthor)}/${encodeURIComponent(blueprintActual.name)}`, {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
                name: blueprintActual.name,
                points: blueprintActual.points
            })
        })
            .then(response => {
                if (!response.ok) {
                    return response.text().then(text => { throw new Error(text); });
                }
                alert("✔️ Plano actualizado con éxito.");
                updateBlueprintsList(currentAuthor);
                fetchAllBlueprints();
            })
            .catch(error => console.error("❌ Error al actualizar:", error));
    }

    function fetchAllBlueprints() {
        $.get("/blueprints")
            .done(function (blueprints) {
                $("#allBlueprintsTable tbody").empty();
                blueprints.forEach(bp => {
                    $("#allBlueprintsTable tbody").append(
                        `<tr>
                        <td>${bp.author}</td>
                        <td>${bp.name}</td>
                        <td>${bp.points.length}</td>
                    </tr>`
                    );
                });
            })
            .fail(error => console.error("❌ Error obteniendo planos:", error));
    }

    function deleteBlueprint() {
        if (!currentBlueprintName || !currentAuthor) {
            alert("No hay un plano seleccionado para eliminar.");
            return;
        }

        const confirmed = confirm(`¿Estás seguro de eliminar el plano: ${currentBlueprintName}?`);
        if (!confirmed) return;

        fetch(`/blueprints/${encodeURIComponent(currentAuthor)}/${encodeURIComponent(currentBlueprintName)}`, {
            method: "DELETE",
        })
            .then(response => {
                if (!response.ok) throw new Error("Error al eliminar el plano.");
                alert("✔️ Plano eliminado correctamente.");

                let canvas = document.getElementById("blueprintCanvas");
                let ctx = canvas.getContext("2d");
                ctx.clearRect(0, 0, canvas.width, canvas.height);

                currentBlueprintName = null;
                blueprintActual = null;
                $("#authorName").text("None");
                updateBlueprintsList(currentAuthor);
                fetchAllBlueprints();
            })
            .catch(error => console.error("❌ Error al eliminar:", error));
    }

    function setAuthor(author) {
        currentAuthor = author;
    }

    function updateBlueprintsList(author) {
        if (!author) {
            console.error("⚠️ No se ha definido un autor.");
            return;
        }

        api.getBlueprintsByAuthor(author, function (blueprints) {
            let transformedData = blueprints.map(bp => ({
                name: bp.name,
                points: bp.points.length
            }));

            let totalPoints = transformedData.reduce((sum, bp) => sum + bp.points, 0);

            $("#blueprintsTable tbody").empty();
            transformedData.forEach(bp => {
                $("#blueprintsTable tbody").append(
                    `<tr>
                        <td>${bp.name}</td>
                        <td>${bp.points}</td>
                        <td>
                            <button class="btn btn-info draw-btn" data-name="${bp.name}">Dibujar</button>
                        </td>
                    </tr>`
                );
            });

            $("#totalUserPoints").text(`Puntos totales: ${totalPoints}`);

            $(".draw-btn").click(function () {
                let blueprintName = $(this).data("name");
                currentBlueprintName = blueprintName;
                api.getBlueprintsByNameAndAuthor(author, blueprintName, drawBlueprint);
            });
        });
    }

    function drawBlueprint(blueprint) {
        let canvas = document.getElementById("blueprintCanvas");
        let ctx = canvas.getContext("2d");

        ctx.clearRect(0, 0, canvas.width, canvas.height);
        ctx.beginPath();

        if (blueprint.points.length > 0) {
            ctx.moveTo(blueprint.points[0].x, blueprint.points[0].y);
            for (let i = 1; i < blueprint.points.length; i++) {
                ctx.lineTo(blueprint.points[i].x, blueprint.points[i].y);
            }
            ctx.stroke();
        }

        blueprintActual = blueprint;

        canvas.onclick = function(event) {
            let rect = canvas.getBoundingClientRect();
            let x = event.clientX - rect.left;
            let y = event.clientY - rect.top;

            blueprintActual.points.push({ x, y });

            drawBlueprint(blueprintActual);
        };
    }

    function init() {
        document.getElementById("saveButton").addEventListener("click", saveOrUpdateBlueprint);
        document.getElementById("deleteButton").addEventListener("click", deleteBlueprint);
        initCanvasHandlers();

        $("#getBlueprintsButton").click(function () {
            let author = $("#authorInput").val().trim();
            if (author) {
                setAuthor(author);
                updateBlueprintsList(author);
                $("#authorName").text(author);
            } else {
                alert("Por favor ingresa un nombre de autor.");
            }
        });

        fetchAllBlueprints();
    }

    return {
        setAuthor,
        updateBlueprintsList,
        drawBlueprint,
        init
    };
})();

window.onload = App.init;
