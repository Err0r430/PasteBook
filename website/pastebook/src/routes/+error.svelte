<script>
    import { page } from '$app/stores';
	import { onMount } from 'svelte';
    import Mode from "../components/Mode.svelte";
	import { ThemeManager } from '$lib/themeManager';

    onMount(() => {
        document.documentElement.style.cssText = ThemeManager.compileVariables();
    });
</script>

<error>
    <h1>{$page.status}</h1>
    <p>{$page.error.message}</p>
    <Mode></Mode>
</error>

<style lang="scss">
  error {
      overflow: hidden;
      overscroll-behavior: none;
      display: flex;
      flex-direction: column;
      height: 100vh;
      justify-content: center;
      background-color: var(--background);

      h1 {
        text-align: center;
        font-size: 30vw;
        font-family: Gabarito, sans-serif;
        font-weight: 800;
        line-height: 25vw;
        margin: 0;
        color: var(--header)
      }

      p {
        text-align: center;
        font-size: 5vw;
        font-family: Gabarito, sans-serif;
        color: var(--subheader);
        font-weight: 500;
        margin: 0;
      }
    }
</style>

<svelte:head>
    <title>Error</title>
    <meta name="description" content="Paste not found.">
    <meta name="theme-color" content="#EE4B2B">
</svelte:head>