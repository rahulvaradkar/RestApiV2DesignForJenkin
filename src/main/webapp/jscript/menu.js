/*
 * PROJECT:   mygosuMenu
 * VERSION:   1.1.1
 * COPYRIGHT: (c) 2003,2004 Cezary Tomczak
 * LINK:      http://gosu.pl/software/mygosumenu.html
 * LICENSE:   BSD (revised)
 */

var menuTimeout = 400;
var menuOffsetWidth = 0;
var menuOffsetHeight = -1;

var menuId = 'menu';
var menuTree = new Array();
var menuSections = new Array();
var menuSectionsCountHide = new Array();
var menuSectionsVisible = new Array();

var menuBrowser = navigator.userAgent.toLowerCase();
var menuBrowserIsOpera = menuBrowser.indexOf('opera') != -1;
var menuBrowserIsIE = menuBrowser.indexOf('msie') != -1 && !menuBrowserIsOpera;
    
function menuInit() {
    menuMakeIds(document.getElementById(menuId).childNodes, menuTree, menuId, 0);
    for (var i = 0; i < menuSections.length; i++) {
        menuSectionsCountHide[menuSections[i]] = 0;
    }
    for (var i = 0; i < menuSections.length; i++) {
        menuInitSection(menuSections[i], menuGetTreeById(menuSections[i]));
    }
}

function menuMakeIds(nodes, tree, id, offsetWidth) {
    for (var i = 0; i < nodes.length; i++) {
        switch (nodes[i].className) {
            case "top":
            case "box-right":
                id = id + "-" + tree.length;
                nodes[i].id = id;
                tree[tree.length] = new Array();
                tree = tree[tree.length - 1];
                break;
            case "box":
                nodes[i].id = id + "-" + tree.length;
                tree[tree.length] = new Array();
                break;
        }
        var className = new String(nodes[i].className);
        if (className == "section") {
            if (!menuBrowserIsIE) {
                nodes[i].style.width = "100%";
            }
            nodes[i].style.left = offsetWidth + menuOffsetWidth;
            nodes[i].style.top = menuOffsetHeight;
        }
        if (className.substr(0, 7) == "section") {
            menuSections[menuSections.length] = id;
            nodes[i].id = id + "-section";
            offsetWidth = nodes[i].offsetWidth;
        }
        if (nodes[i].childNodes) {
            menuMakeIds(nodes[i].childNodes, tree, id, offsetWidth);
        }
    }
}

function menuInitSection(id_section, tree) {
    eval("document.getElementById('"+id_section+"').onmouseover = function() {"+
        "menuShow('"+id_section+"');"+
        "if (document.getElementById('"+id_section+"').className == 'box-right') {"+
            "document.getElementById('"+id_section+"').className = 'box-right-hover';"+
        "}"+
    "}");
    eval("document.getElementById('"+id_section+"').onmouseout = function() {"+
        "setTimeout(\"menuTryHide('"+id_section+"', \"+menuSectionsCountHide['"+id_section+"']+\")\", menuTimeout);"+
        "if (document.getElementById('"+id_section+"').className == 'box-right-hover') {"+
            "document.getElementById('"+id_section+"').className = 'box-right';"+
        "}"+
    "}");
    for (var i = 0; i < tree.length; i++) {
        var id = id_section + "-" + i;
        if (tree[i].length == 0) {
            eval("document.getElementById('"+id+"').onmouseover = function() {"+
                "menuShow('"+id_section+"');"+
                "document.getElementById('"+id+"').className = 'box-hover';"+
            "}");
            eval("document.getElementById('"+id+"').onmouseout = function() {"+
                "setTimeout(\"menuTryHide('"+id_section+"', \"+menuSectionsCountHide['"+id_section+"']+\")\", menuTimeout);"+
                "document.getElementById('"+id+"').className = 'box';"+
            "}");
        }
    }
}

function menuShow(id_section) {
    var sections = menuGetIdParentsArrById(id_section, true);
    for (var i = 0; i < sections.length; i++) {
        menuSectionsCountHide[sections[i]]++;
    }
    for (var i = 0; i < menuSectionsVisible.length; i++) {
        if (!menuArrayContains(sections, menuSectionsVisible[i])) {
            menuHide(menuSectionsVisible[i]);
        }
    }
    document.getElementById(id_section + "-section").style.zIndex = 1;
    document.getElementById(id_section + "-section").style.visibility = 'visible';
    menuSectionsVisible = sections;
}

function menuTryHide(id_section, count) {
    if (count == menuSectionsCountHide[id_section]) {
        var sections = menuGetIdParentsArrById(id_section, true);
        if (menuArraysEqual(sections, menuSectionsVisible)) {
            for (var i = 0; i < sections.length; i++) {
                menuHide(sections[i]);
            }
        } else {
            menuHide(id_section);
        }
    }
}

function menuHide(id_section) {
    document.getElementById(id_section + "-section").style.visibility = 'hidden';
    document.getElementById(id_section + "-section").style.zIndex = -1;
}

function menuGetTreeById(id) {
    var a = id.split("-");
    a.shift();
    var s = "";
    for (var i = 0; i < a.length; i++) {
        s += ("[" + a[i] + "]");
    }
    return eval("menuTree" + s);
}

function menuGetIdParentsArrById(id, includeSelf) {
    var a = id.split("-");
    var ret = new Array();
    if (includeSelf) {
        ret[ret.length] = id;
    }
    while (a.length > 2) {
        a.pop();
        ret[ret.length] = a.join("-");
    }
    return ret;
}

function menuArrayContains(a, s) {
    var found = false;
    for (var i = 0; i < a.length; i++) {
        if (a[i] == s) {
            found = true;
            break;
        }
    }
    return found;
}

function menuArraysEqual(a1, a2) {
    if (a1.length != a2.length) {
        return false;
    }
    for (var i = 0; i < a1.length; i++) {
        if (a1[i] != a2[i]) {
            return false;
        }
    }
    return true;
}