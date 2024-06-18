<script lang="ts">
    import Content from "../../components/Content.svelte";
    import Header from "../../components/Header.svelte";
    import Submit from "../../components/new/Submit.svelte";
    import Switch from "../../components/settings/Switch.svelte";
    import Pulltab from "../../components/pulltab/Pulltab.svelte";
    import Setting from "../../components/settings/Setting.svelte";
    import dropdowns from "$lib/dropdowns.json";

    import {expire, unlisted, wrap} from "$lib/stores";
    import DropDown from "../../components/settings/DropDown.svelte";
    import {onMount} from "svelte";
	import { ThemeManager } from "$lib/themeManager";

    function handleCallback(value: number) {
        expire.set(value);
    }

    let defaultExpire = 86400000;
    let defaultUnlisted = false;
    let forceWrap = false;

    onMount(() => {
        defaultExpire = parseInt(localStorage.getItem("default-expire") ?? "86400000");
        defaultUnlisted = localStorage.getItem("default-unlisted") === "true";
        forceWrap = localStorage.getItem("wrap") === "true";

        document.documentElement.style.cssText = ThemeManager.compileVariables();
    });
</script>

<main>
    <div></div>
    <Header newReport="true" class="header"></Header>
    <Content newReport="true"></Content>
    <Pulltab title="Upload Options">
        <svelte:fragment slot="content">
            <Setting name="Unlisted" description="Hides your paste from the public">
                <svelte:fragment slot="setting">
                    <Switch isSelected={defaultUnlisted} externalHandler={(selected) => {
                        $unlisted = selected;
                    }}></Switch>
                </svelte:fragment>
            </Setting>
            <Setting name="Text Wrap" description="Wraps your text to the edges of display">
                <svelte:fragment slot="setting">
                    <Switch isSelected={forceWrap} externalHandler={(selected) => {
                         $wrap = selected;
                    }}></Switch>
                </svelte:fragment>
            </Setting>
            <Setting name="Expire Time" description="How long your paste should last before being annihilated">
                <svelte:fragment slot="setting">
                    <DropDown callback={(value) => {
                        localStorage.setItem("default-expire", value.toString());
                    }} items={dropdowns.expireOptions} value={defaultExpire} />
                </svelte:fragment>
            </Setting>
        </svelte:fragment>
    </Pulltab>

    <Submit></Submit>
</main>

<style lang="scss">
  div {
    padding-top: 10px + 30px;

    @media (max-width: 600px) {
      padding-top: 27px;
    }
  }

  main {
    flex-direction: column;
    justify-content: center;
    padding: 0;
    height: calc(100vh - 30px);
    margin: 0;
    flex-shrink: inherit;
  }
</style>

<svelte:head>
    <meta property="og:type" content="website"/>
    <meta property="og:title" content="New Paste"/>
    <meta property="og:site_name" content="PasteBook"/>
    <meta property="og:url" content="https://pastebook.dev/new"/>
    <meta property="og:description"
          content="PasteBook is an aesthetic, effortless way to share your blocks of text, and respects your privacy by automatically deleting your pastes."/>
</svelte:head>