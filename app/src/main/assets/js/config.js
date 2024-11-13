export const testbedConfigs = {
    coex: {
        name: "코엑스",
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
            constant_left: {a : 1, b : 274.3702, c : 0.65389},
        },
        big_arrow: {
            width: '300px',
        },
        big_dot: {
            width: '300px',
            constant_top: {a : -1, b : 3191.01896, c : 0.65412},
            constant_left: {a : 1, b : 44.3702, c : 0.65389},
        },
        X: {
            width: '12px',
            left: '-8px', top: '-9px',
            constant_top: {a : -1, b : 2232, c : 1.529},
            constant_left: {a : 1, b : -180, c : 1.529},
        },
        box: {
            constant_top: {a : -1, b : 3424, c : 0.65412},
            constant_left: {a : 1, b : 285, c : 0.65389},
        }
    },
    hansando: {
        name: "한산도함",
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
        "name": "수원역",
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

// mode 종류 정의
export const modeConfig = ["test", "setting", "marker", "history"];

// mode에 따라 보여야될 요소들 정의
export const iconConfig = {
    test: { show: ['big_dot', 'big_arrow', 'box'] },
    setting: { show: ['X', 'small_arrow'] },
    marker: { show: ['X'] },
    history: { show: ['small_dot', 'small_arrow'] }
};