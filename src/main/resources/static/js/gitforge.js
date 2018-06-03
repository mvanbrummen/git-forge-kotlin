var navDropdowns = document.querySelectorAll('.has-dropdown');
var dropdowns = document.querySelectorAll('.dropdown');

handleClick(navDropdowns);
handleClick(dropdowns);


function handleClick(element) {
    element.forEach((d) => {
        d.addEventListener('click', (e) => {
            e.stopPropagation();
            d.classList.toggle('is-active');
            hideOnClickOutside(d);
        });
    });
}

function hideOnClickOutside(element) {
    const outsideClickListener = event => {
        if (!element.contains(event.target)) {
            if (isVisible(element)) {
                element.classList.toggle('is-active');
                removeClickListener();
            }
        }
    }

    const removeClickListener = () => {
        document.removeEventListener('click', outsideClickListener)
    }

    document.addEventListener('click', outsideClickListener)
}

const isVisible = elem => !!elem && !!(elem.offsetWidth || elem.offsetHeight || elem.getClientRects().length)
