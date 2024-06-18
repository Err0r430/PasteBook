<script lang="ts">
    import Header from "../../components/Header.svelte";
    import Switch from "../../components/settings/Switch.svelte";
    import Setting from "../../components/settings/Setting.svelte";
    import {onMount} from "svelte";
    import DropDown from "../../components/settings/DropDown.svelte";
	import { ThemeManager } from "$lib/themeManager";
    import dropdowns from "$lib/dropdowns.json";

    let wrap = false;
    let defaultUnlisted = false;
    let defaultExpire = 86400000;
    let theme: string;

    onMount(() => {

        wrap = localStorage.getItem("wrap") === "true";

        if (wrap) {
            document.body.classList.add("wrap");
        }

        defaultUnlisted = localStorage.getItem("default-unlisted") === "true";

        if (defaultUnlisted) {
            document.body.classList.add("default-unlisted");
        }

        defaultExpire = parseInt(localStorage.getItem("default-expire") ?? "86400000");

        theme = ThemeManager.getTheme();
        
        document.documentElement.style.cssText = ThemeManager.compileVariables();
    });
</script>

<main>
    <div id="padding-container">
        <div id="padding"></div>
    </div>
    <Header title="General Settings" created=""></Header>
    <div id="container">
        <div id="settings">
            <Setting name="Theme" description="Changes the look of PasteBook">
                <svelte:fragment slot="setting">
                    <DropDown callback={(value) => {
                        localStorage.setItem("theme", value);
                        document.documentElement.style.cssText = ThemeManager.compileVariables();
                    }} items={ThemeManager.getThemes()} value={theme} />
                </svelte:fragment>
            </Setting>
            <Setting name="Force Text Wrap" description="Forcefully enable text wrap on all pastes">
                <svelte:fragment slot="setting">
                    <Switch isSelected={wrap} externalHandler={(selected) => {
                        localStorage.setItem("wrap", selected.toString());
                        }}></Switch>
                </svelte:fragment>
            </Setting>
        </div>
    </div>
    <Header title="Default Paste Settings" created=""></Header>
    <div id="container" class="default-settings">
        <div id="settings">
            <Setting name="Default Unlisted" description="Default paste visibility">
                <svelte:fragment slot="setting">
                    <Switch isSelected={defaultUnlisted} externalHandler={(selected) => {
                        localStorage.setItem("default-unlisted", selected.toString());
                        }}></Switch>
                </svelte:fragment>
            </Setting>
            <Setting name="Default Expire Time" description="Default paste expiration time">
                <svelte:fragment slot="setting">
                    <DropDown callback={(value) => {
                        localStorage.setItem("default-expire", value.toString());
                    }} items={dropdowns.expireOptions} value={defaultExpire} />
                </svelte:fragment>
            </Setting>
        </div>
    </div>
</main>

<style lang="scss">

    main {
        height: 100vh;
        background-color: var(--background);
    }

  .default-settings {
    margin-top: 200px;
  }

  #container {
    transition: all 0.5s ease;

    display: block;
    background-color: var(--container-background);
    width: calc(100% - 20px);
    margin: 10px;
    border-radius: 20px;
    animation-iteration-count: 1;
    animation-fill-mode: forwards;
    // height: calc(100% - 140px);
    overflow-x: scroll;
    border: 1px solid var(--container-border);
    box-sizing: border-box;
    padding: 10px 10px 5px;
    opacity: 0;

    @media (max-width: 600px) {
      height: calc(100% - 130px);
      margin-top: 6px;
    }

    animation: fadeIn 0.5s forwards;
  }

  #padding {
    padding-top: 10px + 30px;

    @media (max-width: 600px) {
      padding-top: 7px + 20px;
    }
  }

  @keyframes fadeIn {
    from {
      opacity: 0;
    }

    to {
      opacity: 1;
    }
  }
</style>

<svelte:head>
    <meta property="og:type" content="website"/>
    <meta property="og:title" content="PasteBook Settings"/>
    <meta property="og:site_name" content="PasteBook"/>
    <meta property="og:url" content="https://pastebook.dev/settings"/>
    <meta property="og:description" content="Manage your settings for PasteBook."/>
</svelte:head>