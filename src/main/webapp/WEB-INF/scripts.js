document.addEventListener('DOMContentLoaded', function() {
    const carousels = document.querySelectorAll('.carousel');
    carousels.forEach(function(carousel) {
        // Initialize Bootstrap carousel with custom options
        const bsCarousel = new bootstrap.Carousel(carousel, {
            interval: 5000,  // Changes the speed of the slideshow
            wrap: true,      // Specifies whether the carousel should cycle continuously or have hard stops.
            touch: true      // Enable touch gestures
        });

        // Add event listeners for carousel slide events
        carousel.addEventListener('slide.bs.carousel', function (event) {
            // This event fires immediately when the slide instance method is invoked.
            console.log('Carousel is sliding to ' + event.to); // Debugging the slide event
        });

        carousel.addEventListener('slid.bs.carousel', function (event) {
            // This event is fired when the carousel has completed its slide transition.
            console.log('Carousel has finished sliding to ' + event.to); // Debugging the slid event
        });
    });

    // Initialize swipe gestures for carousel
    handleSwipeGestures(carousels);
});

// Function to handle swipe gestures for a better touch experience on mobile devices
function handleSwipeGestures(carousels) {
    carousels.forEach(function(carousel) {
        let touchStartX = 0;
        let touchEndX = 0;

        carousel.addEventListener('touchstart', function(event) {
            touchStartX = event.changedTouches[0].screenX;
        }, false);

        carousel.addEventListener('touchend', function(event) {
            touchEndX = event.changedTouches[0].screenX;
            handleGesture(carousel);
        }, false);

        function handleGesture(carouselElement) {
            if (touchEndX < touchStartX) {
                bootstrap.Carousel.getInstance(carouselElement).next(); // Move to the next item
            }
            if (touchEndX > touchStartX) {
                bootstrap.Carousel.getInstance(carouselElement).prev(); // Move to the previous item
            }
        }
    });
}