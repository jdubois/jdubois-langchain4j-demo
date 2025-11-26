//
// Place any custom JS here
//

document.addEventListener('DOMContentLoaded', function() {
    const answerElement = document.getElementById('answer-content');
    if (!answerElement) return;

    const answer = answerElement.getAttribute('data-answer');
    if (!answer) return;

    // Check if it's an image URL
    if (isImageUrl(answer)) {
        displayImage(answerElement, answer);
    }
    // Check if it's JSON
    else if (isJSON(answer)) {
        displayJSON(answerElement, answer);
    }
    // Otherwise, display as text
    else {
        displayText(answerElement, answer);
    }
});

function isImageUrl(str) {
    // Check if it's a URL ending with image extensions or contains image service patterns
    return /\.(jpg|jpeg|png|gif|webp|bmp)$/i.test(str) ||
           /^https?:\/\/.+\.(openai|blob|cloudflare|azure|amazonaws)\.com.*\.(jpg|jpeg|png|gif|webp)$/i.test(str) ||
           /^https?:\/\/[^\/]+\/.*image/i.test(str) ||
           /oaidalleapiprodscus\.blob\.core\.windows\.net/i.test(str);
}

function isJSON(str) {
    try {
        const parsed = JSON.parse(str);
        return typeof parsed === 'object' && parsed !== null;
    } catch (e) {
        return false;
    }
}

function displayImage(element, url) {
    const img = document.createElement('img');
    img.src = url;
    img.alt = 'Generated Image';
    img.className = 'img-fluid rounded shadow-sm';
    img.style.maxWidth = '100%';
    img.style.height = 'auto';

    const container = document.createElement('div');
    container.className = 'image-container my-3';
    container.appendChild(img);

    // Add link to open in new tab
    const link = document.createElement('a');
    link.href = url;
    link.target = '_blank';
    link.className = 'btn btn-sm btn-outline-primary mt-2';
    link.textContent = 'Open in new tab';
    container.appendChild(link);

    element.appendChild(container);
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

