import { initCanvasHandlers } from "./canvasHandler.js";

var App = (function () {
    let api = apimock; // Usamos el mock API
    let currentAuthor = "";

    function setAuthor(author) {
        currentAuthor = author;
    }

    function updateBlueprintsList(author) {
        api.getBlueprintsByAuthor(author, function (blueprints) {
            let transformedData = blueprints.map(bp => ({
                name: bp.name,
                points: bp.points.length
            }));

            let totalPoints = transformedData.reduce((sum, bp) => sum + bp.points, 0);

            $("#blueprintsTable tbody").empty(); // Limpiar la tabla
            transformedData.forEach(bp => {
                $("#blueprintsTable tbody").append(
                    `<tr>
                        <td>${bp.name}</td>
                        <td>${bp.points}</td>
                        <td>
                            <button class="btn btn-info draw-btn" data-name="${bp.name}">Draw</button>
                        </td>
                    </tr>`
                );
            });

            $("#totalUserPoints").text(`Total user points: ${totalPoints}`);

            // Asociar evento click a los botones "Draw"
            $(".draw-btn").click(function () {
                let blueprintName = $(this).data("name");
                drawBlueprint(author, blueprintName);
            });
        });
    }

    function drawBlueprint(author, blueprintName) {
        api.getBlueprintsByNameAndAuthor(author, blueprintName, function (blueprint) {
            let canvas = document.getElementById("blueprintCanvas");
            let ctx = canvas.getContext("2d");

            ctx.clearRect(0, 0, canvas.width, canvas.height); // Limpiar el canvas
            ctx.beginPath();
            if (blueprint.points.length > 0) {
                ctx.moveTo(blueprint.points[0].x, blueprint.points[0].y);
                for (let i = 1; i < blueprint.points.length; i++) {
                    ctx.lineTo(blueprint.points[i].x, blueprint.points[i].y);
                }
                ctx.stroke();
            }

            // Actualizar el título del plano seleccionado
            if ($("#selectedBlueprint").length === 0) {
                $("#blueprintCanvas").after(`<h3 id="selectedBlueprint">Drawing: ${blueprintName}</h3>`);
            } else {
                $("#selectedBlueprint").text(`Drawing: ${blueprintName}`);
            }
        });
    }

    function init() {
        initCanvasHandlers(); // Iniciar los manejadores de eventos del canvas
        $("#getBlueprintsButton").click(function () {
            let author = $("#authorInput").val().trim();
            if (author) {
                App.setAuthor(author);
                App.updateBlueprintsList(author);
                $("#authorName").text(author); // ✅ Actualiza el nombre del autor en la página
            } else {
                alert("Please enter an author name.");
            }
        });
    }

    return {
        setAuthor: setAuthor,
        updateBlueprintsList: updateBlueprintsList,
        drawBlueprint: drawBlueprint,
        init: init
    };
})();

// Llamar a la inicialización después de que se cargue la ventana
window.onload = App.init;