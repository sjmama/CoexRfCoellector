export function show_my_position_with_history(x_pos, y_pos) {
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

export function show_my_position(x_pos, y_pos) {
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

export function showArea(x_min, x_max, y_min, y_max) {
    const box = document.getElementById("box");
    const {a: aTop, b: bTop, c: cTop} = config.box.constant_top;
    const {a: aLeft, b: bLeft, c: cLeft} = config.box.constant_left;
    box.style.top = `${(aTop * x_max + bTop) * cTop}px`;
    box.style.left = `${(aLeft * y_min + bLeft) * cLeft}px`;
    box.style.height = `${(x_max - x_min)*cTop}px`
    box.style.width = `${(y_max - y_min)*cLeft}px`
    box.style.display = 'block';
}

export function removeArea(){
    const box = document.getElementById("box");
    box.style.display = 'none';
}