import {iconConfig, testbedConfigs} from "./config.js";

var config = {}

function applyStyles(elementId, styleConfig) {
    const element = document.getElementById(elementId);
    if (element) {
        Object.assign(element.style, styleConfig);
    }
}
function setTestbed(testbedName="coex", floor="B1", mode="test") {
    config = testbedConfigs[testbedName];

    // 스타일 적용
    Object.entries(testbedConfigs[testbedName]).forEach(([elementId, styleConfig]) => {
        applyStyles(elementId, styleConfig);
    });

    // 맵 이미지 변경
    document.getElementById('map_img').src = `./images/maps/${testbedName}/${testbedName}_${floor}F.png`;

    // 아이콘 표시 설정
    document.querySelectorAll('.icon').forEach(icon => {
        icon.style.display = iconConfig[mode].show.includes(icon.id) ? 'block' : 'none';
    });

    const dotContainer = document.getElementById("dotContainer");
    dotContainer.style.left = '-1000px';
    dotContainer.style.top = '-1000px';


    if (mode === "setting" || mode === "marker") {
        enableTouchEvent();
    }
}

export function setDropdownMenu() {
    var select = document.getElementById('testbedSelector');
    for (const key in testbedConfigs) {
        const option = document.createElement("option");
        option.value = key;
        option.textContent = testbedConfigs[key].name;
        select.appendChild(option);
    }
}
export function initTestbedSettings() {
    document.getElementById('testbedSelector').addEventListener('change', function() {
        // alert('선택된 옵션: ' + this.value);
        setTestbed(this.value, "B1", "setting")
    });

// 버튼
    window.onload = function () {
        setTestbed('coex', '0'); // 초기 층 설정
        document.querySelectorAll('.floorBtn').forEach(button => {
            button.addEventListener('click', function() {
                const floor = this.getAttribute('data-floor');
                setTestbed('hansando', floor);
            });
        });
    };

// 버튼 눌렀을 때 색깔 변경
    document.querySelectorAll('.floorBtn').forEach(button => {
        button.addEventListener('click', function() {
            // 모든 버튼에서 'button-active' 클래스 제거
            document.querySelectorAll('.floorBtn').forEach(btn => {
                btn.classList.remove('button-active');
            });

            // 클릭된 버튼에만 'button-active' 클래스 추가
            this.classList.add('button-active');
        });
    });
}
