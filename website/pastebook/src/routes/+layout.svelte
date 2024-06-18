<script>
    import { onMount } from "svelte";
    import Toolbar from "../components/Toolbar.svelte";
	import { ThemeManager } from "$lib/themeManager";
    onMount(() => {
        document.documentElement.style.cssText = ThemeManager.compileVariables();
    });
</script>

<main>
    <Toolbar></Toolbar>
    <slot></slot>
</main>

<style lang="scss">
    main {
        background-color: var(--background);
    }
</style>