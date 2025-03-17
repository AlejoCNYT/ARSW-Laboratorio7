var apimock = (function () {
    const mockData = {
        "johnconnor": [
            {
                name: "house",
                points: [
                    // Base de la casa
                    { x: 100, y: 200 }, { x: 100, y: 300 }, { x: 300, y: 300 }, { x: 300, y: 200 },
                    { x: 100, y: 200 }, { x: 200, y: 100 }, { x: 300, y: 200 },

                    // Puerta
                    { x: 180, y: 300 }, { x: 180, y: 250 }, { x: 220, y: 250 }, { x: 220, y: 300 },

                    // Ventanas
                    { x: 120, y: 220 }, { x: 120, y: 240 }, { x: 140, y: 240 }, { x: 140, y: 220 },
                    { x: 260, y: 220 }, { x: 260, y: 240 }, { x: 280, y: 240 }, { x: 280, y: 220 },
                ]
            },
            {
                name: "gear",
                points: [
                    // Dientes del engranaje
                    { x: 200, y: 100 }, { x: 220, y: 100 }, { x: 220, y: 120 }, { x: 200, y: 120 },
                    { x: 200, y: 100 }, { x: 210, y: 90 }, { x: 220, y: 100 },

                    { x: 280, y: 200 }, { x: 300, y: 200 }, { x: 300, y: 220 }, { x: 280, y: 220 },
                    { x: 280, y: 200 }, { x: 290, y: 190 }, { x: 300, y: 200 },

                    { x: 200, y: 280 }, { x: 220, y: 280 }, { x: 220, y: 300 }, { x: 200, y: 300 },
                    { x: 200, y: 280 }, { x: 210, y: 270 }, { x: 220, y: 280 },

                    { x: 100, y: 200 }, { x: 120, y: 200 }, { x: 120, y: 220 }, { x: 100, y: 220 },
                    { x: 100, y: 200 }, { x: 110, y: 190 }, { x: 120, y: 200 },

                    // Agujero central
                    { x: 200, y: 200 }, { x: 210, y: 200 }, { x: 210, y: 210 }, { x: 200, y: 210 },
                    { x: 200, y: 200 }, { x: 205, y: 195 }, { x: 210, y: 200 },
                ]
            }
        ],
        "sarahconnor": [
            {
                name: "house",
                points: [
                    // Base de la casa
                    { x: 50, y: 150 }, { x: 50, y: 250 }, { x: 150, y: 250 }, { x: 150, y: 150 },
                    { x: 50, y: 150 }, { x: 100, y: 100 }, { x: 150, y: 150 },

                    // Puerta
                    { x: 80, y: 250 }, { x: 80, y: 200 }, { x: 120, y: 200 }, { x: 120, y: 250 },

                    // Ventanas
                    { x: 60, y: 180 }, { x: 60, y: 200 }, { x: 80, y: 200 }, { x: 80, y: 180 },
                    { x: 120, y: 180 }, { x: 120, y: 200 }, { x: 140, y: 200 }, { x: 140, y: 180 },
                ]
            },
            {
                name: "gear",
                points: [
                    // Dientes del engranaje
                    { x: 100, y: 100 }, { x: 120, y: 100 }, { x: 120, y: 120 }, { x: 100, y: 120 },
                    { x: 100, y: 100 }, { x: 110, y: 90 }, { x: 120, y: 100 },

                    { x: 180, y: 100 }, { x: 200, y: 100 }, { x: 200, y: 120 }, { x: 180, y: 120 },
                    { x: 180, y: 100 }, { x: 190, y: 90 }, { x: 200, y: 100 },

                    { x: 100, y: 180 }, { x: 120, y: 180 }, { x: 120, y: 200 }, { x: 100, y: 200 },
                    { x: 100, y: 180 }, { x: 110, y: 170 }, { x: 120, y: 180 },

                    { x: 180, y: 180 }, { x: 200, y: 180 }, { x: 200, y: 200 }, { x: 180, y: 200 },
                    { x: 180, y: 180 }, { x: 190, y: 170 }, { x: 200, y: 180 },

                    // Agujero central
                    { x: 150, y: 150 }, { x: 160, y: 150 }, { x: 160, y: 160 }, { x: 150, y: 160 },
                    { x: 150, y: 150 }, { x: 155, y: 145 }, { x: 160, y: 150 },
                ]
            }
        ]
    };

    return {
        getBlueprintsByAuthor: function (author, callback) {
            console.log("Fetching blueprints for author:", author);
            let blueprints = mockData[author] || [];
            console.log("Blueprints found:", blueprints);
            callback(blueprints);
        },
        getBlueprintsByNameAndAuthor: function (author, blueprintName, callback) {
            var blueprint = mockData[author]?.find(bp => bp.name === blueprintName);
            callback(blueprint);
        },
        updateBlueprints: function (author) {
            console.log(`Updating blueprints for author: ${author}`);
            // Aquí puedes agregar lógica para actualizar los planos si es necesario
        }
    };
})();

/*
Example of use:
var fun = function (list) {
    console.info(list);
};

// Obtener todos los planos de "johnconnor"
apimock.getBlueprintsByAuthor("johnconnor", fun);

// Obtener el plano "house" de "johnconnor"
apimock.getBlueprintsByNameAndAuthor("johnconnor", "house", fun);

// Obtener todos los planos de "sarahconnor"
apimock.getBlueprintsByAuthor("sarahconnor", fun);

// Obtener el plano "gear" de "sarahconnor"
apimock.getBlueprintsByNameAndAuthor("sarahconnor", "gear", fun);
*/