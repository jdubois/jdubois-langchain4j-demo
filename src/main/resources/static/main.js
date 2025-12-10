//
// Place any custom JS here
//

document.addEventListener('DOMContentLoaded', function() {
    const answerElement = document.getElementById('answer-content');
    if (!answerElement) return;

    const answer = answerElement.getAttribute('data-answer');
    if (!answer) return;

    // Check if it's JSON
    else if (isJSON(answer)) {
        displayJSON(answerElement, answer);
    }
    // Otherwise, display as text
    else {
        displayText(answerElement, answer);
    }
});

function isJSON(str) {
    try {
        const parsed = JSON.parse(str);
        return typeof parsed === 'object' && parsed !== null;
    } catch (e) {
        return false;
    }
}

function displayJSON(element, jsonStr) {
    const data = JSON.parse(jsonStr);

    const pre = document.createElement('pre');
    pre.className = 'json-display p-3 bg-light border rounded';
    pre.style.overflowX = 'auto';

    const code = document.createElement('code');
    code.textContent = JSON.stringify(data, null, 2);
    code.className = 'language-json';

    pre.appendChild(code);
    element.appendChild(pre);
}

function displayText(element, text) {
    const p = document.createElement('p');
    p.className = 'text-display';
    // Preserve line breaks
    p.style.whiteSpace = 'pre-wrap';
    p.textContent = text;

    element.appendChild(p);
}

