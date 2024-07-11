<script lang="ts">
    import {onMount} from "svelte";

    export let title: string;

    onMount(() => {
        let content = document.querySelector("content") as HTMLDivElement;

        content.style.top = `calc(100% + ${(content.clientHeight / 2) + 1}px)`;
    });

    let open = false;
    let cooldown = false;


    function toggleClick() {
        let content = document.querySelector("content") as HTMLDivElement;
        let blur = document.querySelector(".blur") as HTMLDivElement;
        let tab = document.querySelector(".tab") as HTMLButtonElement;

        if (open) {
            content.style.top = `calc(100% + ${(content.clientHeight / 2) + 1}px)`;

            blur.style.backgroundColor = "rgba(0, 0, 0, 0)"

            cooldown = true;
            setTimeout(() => {
                blur.style.display = "none";
                tab.classList.add("non-active");
                cooldown = false;
            }, 500)

            open = false;
        } else {
            if (cooldown) return;
            content.style.top = `50%`;

            blur.style.display = "block";

            setTimeout(() => {
                blur.style.backgroundColor = "rgba(0, 0, 0, 0.5)";
            }, 1)

            tab.classList.remove("non-active");

            open = true;
        }
    }
</script>

<contentcontainer>
    <content>
        <button class="tab non-active" on:click={toggleClick}>{title}</button>
        <slot name="content"></slot>
    </content>
    <button class="blur" on:click={toggleClick}></button>
</contentcontainer>

<style lang="scss">
  .blur {
    transition: background-color 0.5s ease;
    position: fixed;
    top: 0;
    background-color: var(--components-pulltab-blur);
    width: 100vw;
    height: 100vh;
    z-index: 1001;
    outline: none;
    border: none;
    display: none;
  }

  .tab {
    transition: all 0.5s ease;

    font: inherit;
    cursor: pointer;
    outline: none;

    position: fixed;
    margin: 0;
    top: -35px;
    height: 35.5px;
    width: 140px;
    bottom: 0;
    padding: 5px 5px 10px;
    left: calc(50% - 70px);
    border-top-right-radius: 10px;
    border-top-left-radius: 10px;
    color: var(--components-pulltab-text);
    z-index: 1003;

    border-left: 1px solid var(--components-pulltab-border);
    border-right: 1px solid var(--components-pulltab-border);
    border-top: 1px solid var(--components-pulltab-border);
    border-bottom: none;

    animation: fadeIn 0.6s ease;

    background-color: var(--components-pulltab-background);

    :hover & {
      &.non-active {
        background-color: var(--components-pulltab-hover);

        top: -32px;
      }
    }
  }

  content {
    z-index: 1003;
    width: 65vw;
    position: fixed;
    top: 120%;
    left: 50%;
    background-color: var(--components-pulltab-background);
    transition: all 0.5s ease;

    @media (max-width: 500px) {
      width: 91vw;
    }

    border: 1px solid var(--components-pulltab-border);

    border-radius: 20px;
    transform: translate(-50%, -50%);
    padding: 10px;
    font-family: Gabarito, sans-serif;
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