// =========================================
// LAYOUT SYSTEM
// =========================================

document.addEventListener("DOMContentLoaded", async () => {

    // Sidebar placeholder
    const sidebarContainer =
        document.getElementById(
            "sidebar-container"
        );

    // Pokud existuje
    if (sidebarContainer) {

        try {

            // Načti sidebar
            const response = await fetch(
                "/components/sidebar.html"
            );

            // HTML obsah
            const sidebarHtml =
                await response.text();

            // Vlož sidebar
            sidebarContainer.innerHTML =
                sidebarHtml;

            // =========================================
            // ACTIVE NAVIGATION
            // =========================================

            const currentPage =
                window.location.pathname;

            const navLinks =
                document.querySelectorAll(
                    ".nav-item"
                );

            navLinks.forEach(link => {

                const linkPath =
                    link.getAttribute("href");

                if (
                    currentPage.includes(linkPath)
                ) {

                    link.classList.add(
                        "active"
                    );
                }

            });

        } catch (error) {

            console.error(
                "Layout error:",
                error
            );

        }

    }

    // =========================================
    // TABS SYSTEM
    // =========================================

    const tabButtons =
        document.querySelectorAll(
            ".tab-button"
        );

    tabButtons.forEach(button => {

        button.addEventListener(
            "click",
            () => {

                const target =
                    button.dataset.tab;

                tabButtons.forEach(btn => {

                    btn.classList.remove(
                        "active"
                    );

                });

                document
                    .querySelectorAll(
                        ".tab-content"
                    )
                    .forEach(content => {

                        content.classList.remove(
                            "active"
                        );

                    });

                button.classList.add(
                    "active"
                );

                document
                    .getElementById(target)
                    .classList.add("active");

            }
        );

    });

});

// =========================================
// NOT IMPLEMENTED HELPER
// =========================================

window.notImplemented = function () {

    window.location.href =
        "../pages/error.html";

};