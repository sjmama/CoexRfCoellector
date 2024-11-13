// 테스트베드별 설정 값
const testbedConfigs = {
    coex: {
        x_y_axis_rotation : false,
        map_area: {
            left: '0px', top: '0px'
        },
        small_arrow: {
            width: '40px',
            left:'2px', top: '-22px',
            transformOrigin: '-3px 20px',
        },
        small_dot: {
            width: '10px',
            left: '-5px', top: '-6px',
            constant_top: {a : -1, b : 3413.01896, c : 0.65412},
            constant_left: {a : 1, b : 202.3702, c : 0.65550},
        },
        big_arrow: {
            width: '300px',
        },
        big_dot: {
            width: '300px',
            constant_top: {a : -1, b : 3181.01896, c : 0.65412},
            constant_left: {a : 1, b : -27.6298, c : 0.65550},
        },
        X: {
            width: '12px',
            left: '-8px', top: '-9px',
            constant_top: {a : -1, b : 2232, c : 1.5288},
            constant_left: {a : 1, b : -132.91, c : 1.52539},
        },
        box: {
            constant_top: {a : -1, b : 3424, c : 0.65412},
            constant_left: {a : 1, b : 213, c : 0.65389},
        }
    },
    hansando: {
        x_y_axis_rotation : true,
        map_area: {
            left: '-200px', top: '0px',
        },
        small_arrow: {
            width: '40px',
            left:'2px', top: '-22px',
            transformOrigin: '-3px 20px',
        },
        small_dot: {
            width: '10px',
            left: '-5px', top: '-6px',
            constant_top: {a : -1, b : 1555, c : 0.92613636},
            constant_left: {a : 1, b : 225, c : 0.92613636},
        },
        big_arrow: {
            width: '120px',
        },
        big_dot: {
            width: '120px',
            constant_top: {a : -1, b : 1488, c : 0.92613636},
            constant_left: {a : 1, b : 160, c : 0.92613636},
        },
        X: {
            width: '12px',
            left: '-8px', top: '-9px',
            constant_top: {a : -1, b : 1438, c : 1.0797546},
            constant_left: {a : 1, b : -210, c : 1.0797546},
        },
        box: {
            constant_top: {a : 1, b : 0, c : 1},
            constant_left: {a : 1, b : 0, c : 1},
        }
    },
    suwonstation: {
        x_y_axis_rotation : false,
        map_area: {
            left: '-200px', top: '0px',
        },
        small_arrow: {
            width: '40px',
            left:'2px', top: '-22px',
            transformOrigin: '-3px 20px',
        },
        small_dot: {
            width: '10px',
            left: '-5px', top: '-6px',
            constant_top: {a : 1, b : 296, c : 0.81},
            constant_left: {a : 1, b : -623, c : 0.81},
        },
        big_arrow: {
            width: '120px',
        },
        big_dot: {
            width: '120px',
            constant_top: {a : 0, b : 0, c : 0},
            constant_left: {a : 0, b : 0, c : 0},
        },
        X: {
            width: '12px',
            left: '-8px', top: '-9px',
            constant_top: {a : 1, b : -238.68, c : 1.2345679},
            constant_left: {a : 1, b : 502.9, c : 1.2345679},
        },
        box: {
            constant_top: {a : 1, b : 0, c : 1},
            constant_left: {a : 1, b : 0, c : 1},
        }
    },
    hanasquare: {
        x_y_axis_rotation : false,
        map_area: {
            left: '-200px', top: '0px',
        },
        small_arrow: {
            width: '40px',
            left:'2px', top: '-22px',
            transformOrigin: '-3px 20px',
        },
        small_dot: {
            width: '10px',
            left: '-5px', top: '-6px',
            constant_top: {a : 1, b : 296, c : 0.81},
            constant_left: {a : 1, b : -623, c : 0.81},
        },
        big_arrow: {
            width: '120px',
        },
        big_dot: {
            width: '120px',
            constant_top: {a : 0, b : 0, c : 0},
            constant_left: {a : 0, b : 0, c : 0},
        },
        X: {
            width: '12px',
            left: '-8px', top: '-9px',
            constant_top: {a : 1, b : -238.68, c : 1.2345679},
            constant_left: {a : 1, b : 502.9, c : 1.2345679},
        },
        box: {
            constant_top: {a : 1, b : 0, c : 1},
            constant_left: {a : 1, b : 0, c : 1},
        }
    },

};

var clicked_x = 0
var clicked_y = 0
var prev_x = 0
var prev_y = 0
var config = {}
var currentAngle = 0; // 현재 회전 각도를 추적하기 위한 변수
var markers = [];
var edges = [];
var draw_line_mode = true;
var clicked_marker;

function applyStyles(elementId, styleConfig) {
    const element = document.getElementById(elementId);
    if (element) {
        Object.assign(element.style, styleConfig);
    }
}

// 테스트베드 이름, 층 정보를 세팅
// mode에는 "test", "setting", "history"를 넣을 수 있음.
// [mode 설명]
// "test" : 큰 점, 큰 화살표 표시 (실시간 테스트를 위한 모드. 점 하나만 표시)
// "setting" : X, 작은 화살표 표시 (맵 수집을 위한 모드. 시작 위치/방향 설정 모드)
// "history" : 작은 점, 작은 화살표 표시 (맵 수집을 위한 모드. 점 history들 표시)
window.setTestbed = function (testbedName="coex", floor="B1", mode="test") {
    config = testbedConfigs[testbedName];

    // mode에 따라 보여야될 요소들 정의
    const modeConfig = {
        test: { show: ['big_dot', 'big_arrow', 'box'] },
        setting: { show: ['X', 'small_arrow'] },
        marker: { show: ['X'] },
        history: { show: ['small_dot', 'small_arrow'] }
    };

    // 스타일 적용
    Object.entries(testbedConfigs[testbedName]).forEach(([elementId, styleConfig]) => {
        applyStyles(elementId, styleConfig);
    });

    // 맵 이미지 변경
    document.getElementById('map_img').src = `./images/maps/${testbedName}/${testbedName}_${floor}F.png`;

    // 아이콘 표시 설정
    document.querySelectorAll('.icon').forEach(icon => {
        icon.style.display = modeConfig[mode].show.includes(icon.id) ? 'block' : 'none';
    });

    const dotContainer = document.getElementById("dotContainer");
    dotContainer.style.left = '-1000px';
    dotContainer.style.top = '-1000px';


    if (mode === "setting" || mode === "marker") {
        enableTouchEvent(mode);
    }
}


// 클릭 또는 터치하고자 하는 곳에 X 이미지를 띄우고 싶다면 아래 함수 맨 처음에 호출
function enableTouchEvent(mode) {
    const isTouchDevice = (navigator.maxTouchPoints || 'ontouchstart' in document.documentElement);

    if (document.readyState === "loading") {  // If document is still loading, wait for it to complete
        document.addEventListener("DOMContentLoaded", function () {
            document.removeEventListener("DOMContentLoaded", arguments.callee, false);
            setupTouchEvent(isTouchDevice);
        }, false);
    } else {  // `DOMContentLoaded` has already fired
        setupTouchEvent(isTouchDevice, mode);
    }
}

function setupTouchEvent(is_touch_device, mode="setting") {
    function handleEvent(event) {
        let x, y;
        if (event.type === 'touchstart') {
            // 터치 이벤트의 경우
            const touch = event.touches[0]; // 첫 번째 터치 포인트
            x = touch.pageX - this.offsetLeft;
            y = touch.pageY - this.offsetTop;
        } else {
            // 마우스 이벤트의 경우
            x = event.pageX - this.offsetLeft;
            y = event.pageY - this.offsetTop;
        }
        const dotContainer = document.getElementById("dotContainer");
        Object.assign(dotContainer.style, {
            left: `${x - 6}px`,
            top: `${y - 8}px`
        });
        function calculateCoordinate(value, config) {
            return (config.a * Number(value.replace("px", "")) + config.b) * config.c;
        }
        if (!config.x_y_axis_rotation) {
            clicked_x = calculateCoordinate(dotContainer.style.top, config.X.constant_top);
            clicked_y = calculateCoordinate(dotContainer.style.left, config.X.constant_left);
        } else {
            clicked_y = calculateCoordinate(dotContainer.style.top, config.X.constant_top);
            clicked_x = calculateCoordinate(dotContainer.style.left, config.X.constant_left);
        }

        console.log(clicked_x, clicked_y);

        // "marker" 모드일 때,
        if (mode === "marker") {
            // SVG 컨테이너 찾기
            const svgContainer = document.getElementById('svgContainer');

            // 클릭한 점이 존재해 있던 점인지 확인하기
            const existingMarker = markers.find(marker =>
                Math.abs(marker.x - clicked_x) < 10 && Math.abs(marker.y - clicked_y) < 10
            );

            if (existingMarker) {
                if (markers.length > 1 && draw_line_mode) {
                    // const secondLastMarker = markers[markers.length - 1];
                    // if (existingMarker === secondLastMarker) {
                    if (existingMarker === clicked_marker) {
                        return;
                    }
                    edges.push({
                        start: clicked_marker,
                        end: existingMarker
                    });
                    const newLine = document.createElementNS('http://www.w3.org/2000/svg', 'line');
                    newLine.setAttribute('x1', prev_x);
                    newLine.setAttribute('y1', prev_y);
                    if (!config.x_y_axis_rotation) {
                        newLine.setAttribute('x2', (existingMarker.y/config.X.constant_left.c-config.X.constant_left.b)*config.X.constant_left.a-2);
                        newLine.setAttribute('y2', (existingMarker.x/config.X.constant_top.c-config.X.constant_top.b)*config.X.constant_top.a-1);
                    }
                    else {
                        newLine.setAttribute('x2', (existingMarker.x/config.X.constant_left.c-config.X.constant_left.b)*config.X.constant_left.a-2);
                        newLine.setAttribute('y2', (existingMarker.y/config.X.constant_top.c-config.X.constant_top.b)*config.X.constant_top.a-1);
                    }

                    newLine.setAttribute('stroke', 'black'); // 선의 색상
                    newLine.setAttribute('stroke-width', 2); // 선의 두께
                    svgContainer.appendChild(newLine);
                }
                else {
                    draw_line_mode = true;
                    clicked_marker = existingMarker;
                }

                if (!config.x_y_axis_rotation) {
                    prev_x = (existingMarker.y/config.X.constant_left.c-config.X.constant_left.b)*config.X.constant_left.a -2
                    prev_y = (existingMarker.x/config.X.constant_top.c-config.X.constant_top.b)*config.X.constant_top.a -1
                }
                else {
                    prev_x = (existingMarker.x/config.X.constant_left.c-config.X.constant_left.b)*config.X.constant_left.a -2
                    prev_y = (existingMarker.y/config.X.constant_top.c-config.X.constant_top.b)*config.X.constant_top.a -1
                }


                clicked_marker = {x:clicked_x, y:clicked_y}

                return; // 기존 마커가 눌린 경우, 새로운 마커를 생성하지 않음
            }

            markers.push({x:clicked_x, y:clicked_y});


            const newMarker = document.createElementNS('http://www.w3.org/2000/svg', 'circle');
            newMarker.setAttribute("class", "marker")
            newMarker.setAttribute('cx', x-8); // 클릭된 x 좌표
            newMarker.setAttribute('cy', y-9); // 클릭된 y 좌표
            newMarker.setAttribute('r', 10); // 마커의 반지름
            newMarker.setAttribute('fill', 'blue'); // 마커의 색상
            svgContainer.appendChild(newMarker);

            if (markers.length > 1 && draw_line_mode) {
                const lastMarker = markers[markers.length - 1];
                const secondLastMarker = markers[markers.length - 2];
                edges.push({
                    start: secondLastMarker,
                    end: lastMarker
                });
                const newLine = document.createElementNS('http://www.w3.org/2000/svg', 'line');
                newLine.setAttribute('x1', prev_x);
                newLine.setAttribute('y1', prev_y);
                newLine.setAttribute('x2', x-8);
                newLine.setAttribute('y2', y-9);
                newLine.setAttribute('stroke', 'black'); // 선의 색상
                newLine.setAttribute('stroke-width', 2); // 선의 두께
                svgContainer.appendChild(newLine);
            }
            else {
                draw_line_mode = true;
            }

            prev_x = x-8
            prev_y = y-9
            clicked_marker = {x:clicked_x, y:clicked_y}
        }
    }
    function handleEventContextMenu(event) {
        draw_line_mode = false;
        event.preventDefault();
        let x, y;
        // 마우스 이벤트의 경우
        x = event.pageX - this.offsetLeft;
        y = event.pageY - this.offsetTop;

        const dotContainer = document.getElementById("dotContainer");
        Object.assign(dotContainer.style, {
            left: `${x - 6}px`,
            top: `${y - 8}px`
        });
        function calculateCoordinate(value, config) {
            return (config.a * Number(value.replace("px", "")) + config.b) * config.c;
        }
        if (!config.x_y_axis_rotation) {
            clicked_x = calculateCoordinate(dotContainer.style.top, config.X.constant_top);
            clicked_y = calculateCoordinate(dotContainer.style.left, config.X.constant_left);
        } else {
            clicked_y = calculateCoordinate(dotContainer.style.top, config.X.constant_top);
            clicked_x = calculateCoordinate(dotContainer.style.left, config.X.constant_left);
        }

        // console.log(clicked_x, clicked_y);

        // "marker" 모드일 때,
        if (mode === "marker") {
            // 클릭한 점이 존재해 있던 점인지 확인하기
            const existingMarker = markers.find(marker =>
                Math.abs(marker.x - clicked_x) < 10 && Math.abs(marker.y - clicked_y) < 10
            );

            if (existingMarker) {
                const markerIndex = markers.findIndex(marker => marker.x === existingMarker.x && marker.y === existingMarker.y);
                const svgContainer = document.getElementById('svgContainer');

                // Find the actual DOM node corresponding to the existing marker
                const allMarkers = Array.from(svgContainer.getElementsByTagName('circle'));
                const markerElement = allMarkers.find(element =>
                    Math.abs(parseFloat(element.getAttribute('cx')) - (existingMarker.y / config.X.constant_left.c - config.X.constant_left.b) * config.X.constant_left.a + 8) < 10 &&
                    Math.abs(parseFloat(element.getAttribute('cy')) - (existingMarker.x / config.X.constant_top.c - config.X.constant_top.b) * config.X.constant_top.a + 9) < 10
                );

                if (markerElement) {
                    svgContainer.removeChild(markerElement);
                }

                // Remove related edges
                const linesToRemove = edges.filter(edge => edge.start === existingMarker || edge.end === existingMarker);
                linesToRemove.forEach(line => {
                    const lineElements = Array.from(svgContainer.getElementsByTagName('line'));
                    lineElements.forEach(lineElement => {
                        const x1 = parseFloat(lineElement.getAttribute('x1'));
                        const y1 = parseFloat(lineElement.getAttribute('y1'));
                        const x2 = parseFloat(lineElement.getAttribute('x2'));
                        const y2 = parseFloat(lineElement.getAttribute('y2'));

                        const startX = Math.round(line.start.y / config.X.constant_left.c - config.X.constant_left.b) * config.X.constant_left.a - 2;
                        const startY = Math.round(line.start.x / config.X.constant_top.c - config.X.constant_top.b) * config.X.constant_top.a - 1;
                        const endX = Math.round(line.end.y / config.X.constant_left.c - config.X.constant_left.b) * config.X.constant_left.a - 2;
                        const endY = Math.round(line.end.x / config.X.constant_top.c - config.X.constant_top.b) * config.X.constant_top.a  - 1;
                        console.log(`start : ${startX}, ${startY} / end : ${endX}, ${endY}`)
                        if ((x1 === startX && y1 === startY && x2 === endX && y2 === endY) ||
                            (x1 === endX && y1 === endY && x2 === startX && y2 === startY)) {
                            svgContainer.removeChild(lineElement);
                        }
                    });
                });

                // Remove from markers and edges arrays
                markers.splice(markerIndex, 1);
                edges = edges.filter(edge => edge.start !== existingMarker && edge.end !== existingMarker);
            }
        }
    }
    const map_img = document.getElementsByTagName("body")[0];
    map_img.addEventListener(is_touch_device ? 'touchstart' : 'click', handleEvent);
    map_img.addEventListener('contextmenu', handleEventContextMenu);
}

// 점 또는 X 이미지에 화살표의 각도 설정
window.rotateArrow = function (targetAngle) {
    const arrow = document.querySelector('#small_arrow, #big_arrow').style.display === 'block' ?
        document.querySelector('#small_arrow') : document.querySelector('#big_arrow');

    // 현재 각도와 목표 각도 사이의 차이를 계산합니다.
    var angleDifference = targetAngle - currentAngle;
    // -180도와 +180도 사이의 범위로 각도 차이를 정규화합니다.
    angleDifference = (angleDifference + 180) % 360 - 180;

    // 최소 회전을 위해 각도 차이가 180보다 크면 360에서 각도 차이를 뺍니다.
    if (angleDifference > 180) {
        angleDifference -= 360;
    }
    // 최소 회전을 위해 각도 차이가 -180보다 작으면 360을 더합니다.
    else if (angleDifference < -180) {
        angleDifference += 360;
    }

    // 현재 각도에 각도 차이를 더하여 새로운 회전 각도를 계산합니다.
    currentAngle += angleDifference;

    // 화살표 이미지를 새로운 각도로 회전시킵니다.
    if (!config.x_y_axis_rotation)
        arrow.style.transform = `rotate(${currentAngle}deg)`;
    else
        arrow.style.transform = `rotate(${currentAngle - 90}deg)`;
}

// 터치한 곳의 좌표값을 가져오기 위한 함수 (안드로이드 전용)
window.getClickedPosition = function () {
    return clicked_x + "\t" + clicked_y
}

window.show_my_position_with_history = function (x_pos, y_pos) {
    const dot = document.getElementById("dotContainer");
    const {a: aTop, b: bTop, c: cTop} = config.small_dot.constant_top;
    const {a: aLeft, b: bLeft, c: cLeft} = config.small_dot.constant_left;
    if (!config.x_y_axis_rotation) {
        dot.style.top = `${(aTop * x_pos + bTop) * cTop}px`;
        dot.style.left = `${(aLeft * y_pos + bLeft) * cLeft}px`;
    }
    else {
        dot.style.top = `${(aTop * y_pos + bTop) * cTop}px`;
        dot.style.left = `${(aLeft * x_pos + bLeft) * cLeft}px`;
    }


    const smallDot = document.getElementById("small_dot").cloneNode(true); // small_dot을 복제
    smallDot.style.position = "absolute"; // 복제된 small_dot의 위치 설정을 절대 위치로 변경
    smallDot.style.zIndex = "6"
    smallDot.style.top = `${parseFloat(dot.style.top)+2}px`;
    smallDot.style.left = `${parseFloat(dot.style.left)+3}px`;

    document.body.appendChild(smallDot); // 복제된 small_dot을 body에 추가
}

window.show_my_position = function (x_pos, y_pos) {
    const dot = document.getElementById("dotContainer");
    if (dot.style.display === "none")
        dot.style.display = "block"
    const {a: aTop, b: bTop, c: cTop} = config.big_dot.constant_top;
    const {a: aLeft, b: bLeft, c: cLeft} = config.big_dot.constant_left;
    if (!config.x_y_axis_rotation) {
        dot.style.top = `${(aTop * x_pos + bTop) * cTop}px`;
        dot.style.left = `${(aLeft * y_pos + bLeft) * cLeft}px`;
    }
    else {
        dot.style.top = `${(aTop * y_pos + bTop) * cTop}px`;
        dot.style.left = `${(aLeft * x_pos + bLeft) * cLeft}px`;
    }
}

window.showArea = function (x_min, x_max, y_min, y_max) {
    const box = document.getElementById("box");
    const {a: aTop, b: bTop, c: cTop} = config.box.constant_top;
    const {a: aLeft, b: bLeft, c: cLeft} = config.box.constant_left;
    box.style.top = `${(aTop * x_max + bTop) * cTop}px`;
    box.style.left = `${(aLeft * y_min + bLeft) * cLeft}px`;
    box.style.height = `${(x_max - x_min)*cTop}px`
    box.style.width = `${(y_max - y_min)*cLeft}px`
    box.style.display = 'block';
}

window.removeArea = function (){
    const box = document.getElementById("box");
    box.style.display = 'none';
}
window.exportJson = function() {
    console.log(markers)
    console.log(edges)
    const jsonOutput = {
        nodes: markers.map((marker, index) => ({ id: `Node${index + 1}`, coords: [marker.x, marker.y] })),
        edges: edges.map((edge, index) => ({
            start: `Node${markers.findIndex(marker => marker.x === edge.start.x && marker.y === edge.start.y) + 1}`,
            end: `Node${markers.findIndex(marker => marker.x === edge.end.x && marker.y === edge.end.y) + 1}`,
            distance: Math.sqrt(Math.pow(edge.end.x - edge.start.x, 2) + Math.pow(edge.end.y - edge.start.y, 2))
        }))
    };

    console.log(JSON.stringify(jsonOutput, null, 2));
    // 이 코드는 JSON을 콘솔에 출력합니다.
    // 웹 페이지에서 사용자가 직접 다운로드할 수 있도록 만들려면 추가 로직이 필요합니다.
}

// JSON 데이터를 인자로 받아 markers와 edges 배열에 데이터를 저장하는 함수
window.loadJson = function (jsonData) {
    // markers 배열 초기화 및 데이터 저장 (id 없이)
    markers = jsonData.nodes.map(node => ({
        x: config.x_y_axis_rotation? node.coords[1] : node.coords[0],
        y: config.x_y_axis_rotation? node.coords[0] : node.coords[1]
    }));
    console.log(markers)
    // edges 배열 초기화 및 데이터 저장 (distance 없이)
    edges = jsonData.edges.map(edge => {
        const startMarker = markers.find((marker, index) => `Node${index + 1}` === edge.start);
        const endMarker = markers.find((marker, index) => `Node${index + 1}` === edge.end);
        return {
            start: { x: startMarker.x, y: startMarker.y },
            end: { x: endMarker.x, y: endMarker.y }
        };
    });
    console.log(edges)


    // 마커와 엣지를 SVG에 추가
    renderMarkersAndEdges();
    draw_line_mode = false;
}

// 마커와 엣지를 SVG에 추가하는 함수
function renderMarkersAndEdges() {
    const svgContainer = document.getElementById('svgContainer');

    // 마커 추가
    markers.forEach(marker => {
        const newMarker = document.createElementNS('http://www.w3.org/2000/svg', 'circle');
        newMarker.setAttribute("class", "marker");
        newMarker.setAttribute('cx', (marker.y / config.X.constant_left.c - config.X.constant_left.b) * config.X.constant_left.a - 2);
        newMarker.setAttribute('cy', (marker.x / config.X.constant_top.c - config.X.constant_top.b) * config.X.constant_top.a - 1);
        newMarker.setAttribute('r', 10);
        newMarker.setAttribute('fill', 'blue');
        svgContainer.appendChild(newMarker);

    });

    // 엣지 추가
    edges.forEach(edge => {
        const newLine = document.createElementNS('http://www.w3.org/2000/svg', 'line');
        newLine.setAttribute('x1', (edge.start.y / config.X.constant_left.c - config.X.constant_left.b) * config.X.constant_left.a - 2);
        newLine.setAttribute('y1', (edge.start.x / config.X.constant_top.c - config.X.constant_top.b) * config.X.constant_top.a - 1);
        newLine.setAttribute('x2', (edge.end.y / config.X.constant_left.c - config.X.constant_left.b) * config.X.constant_left.a - 2);
        newLine.setAttribute('y2', (edge.end.x / config.X.constant_top.c - config.X.constant_top.b) * config.X.constant_top.a - 1);
        newLine.setAttribute('stroke', 'black');
        newLine.setAttribute('stroke-width', 2);
        svgContainer.appendChild(newLine);
    });
}





// 페이지 로드 시 자동으로 특정 테스트베드 설정 적용
window.onload = function () {
    setTestbed('coex', "B1"); // 기본적으로 적용할 테스트베드 이름을 여기에 입력
//     setTestbed('hansando', "0", "history"); // 기본적으로 적용할 테스트베드 이름을 여기에 입력
    // rotateArrow(0)
    // setTestbed('hansando', floor="0", mode="test"); // 기본적으로 적용할 테스트베드 이름을 여기에 입력
//    show_my_position_with_history(0, 0)
    // show_my_position_with_history(0, 4306)
    // show_my_position_with_history(2663.5, 4306)
    // show_my_position_with_history(1988.5, 6278)
    // show_my_position_with_history(1988.5, 6500)
    // show_my_position(0, 0)
    // show_my_position(0, 4306)
    // show_my_position(2663.5, 4306)
    // show_my_position(1988.5, 6278)
    // showArea(0, 5790, 0, 5790)
};



// 점 찍기 함수
function plotPoint(x, y) {
    const dot = document.createElement('div');

    dot.style.left = `${(4154/6354)*y+175}px`;
    dot.style.top = `${2230-x*(2006/3058)}px`;
    dot.style.position = 'absolute';

    dot.style.width = '10px'; // 점의 크기
    dot.style.height = '10px';
    dot.style.zIndex = 3;
    dot.style.backgroundColor = 'blue'; // 점의 색상
    dot.className = 'dot1'; // 점을 식별할 수 있는 클래스 추가
    document.body.appendChild(dot); // 생성된 점을 문서에 추가
}
function plotPointRed(x, y) {
    document.querySelectorAll('.dot2').forEach(function(dot) {
        dot.remove();
    });
    const dot = document.createElement('div');

    dot.style.left = `${(4154/6354)*y+175}px`;
    dot.style.top = `${2230-x*(2006/3058)}px`;
    dot.style.position = 'absolute';

    dot.style.width = '10px'; // 점의 크기
    dot.style.height = '10px';
    dot.style.zIndex = 3;
    dot.style.backgroundColor = 'red'; // 점의 색상
    dot.className = 'dot2'; // 점을 식별할 수 있는 클래스 추가
    document.body.appendChild(dot); // 생성된 점을 문서에 추가
}

// 모든 점 지우기 함수
function clearPoints() {
    const dots = document.querySelectorAll('.dot2'); // 문서에서 모든 점을 찾습니다.
    dots.forEach(dot => dot.remove()); // 각 점에 대해 반복하면서 문서에서 제거

    const dots2 = document.querySelectorAll('.dot1'); // 문서에서 모든 점을 찾습니다.
    dots2.forEach(dot => dot.remove()); // 각 점에 대해 반복하면서 문서에서 제거
}