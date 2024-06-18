<script lang="ts">
    import {severes, validScan, warnings, wrap, writableContent} from "$lib/stores.ts"
    import detection from "$lib/detections.json"

    import type {Issue} from "$lib/issue";
    import {onMount} from "svelte";

    import {page} from '$app/stores';

    const scan = $page.url.searchParams.has('inspect');

    export let content: string = "No content provided"
    export let reportBook: boolean = false
    export let newReport: boolean = false;
    export let wrapPre: boolean = false;

    const results: Issue[] = [];

    for (const key in detection) {
        let issue = {
            id: detection[key].filename,
            visual: detection[key].visual,
            description: detection[key].description,
            severity: detection[key].severity
        }

        results.push(issue)
    }

    onMount(() => {
        if (localStorage.getItem("wrap") !== null && localStorage.getItem("wrap") === "true") {
            wrap.set(true)
            wrapPre = true
        }

        wrap.subscribe((value) => {
            let textArea = document.querySelector(".input") as HTMLTextAreaElement | null

            if (textArea === null) {
                return;
            }

            if (value === true) {
                textArea.style.whiteSpace = "break-spaces"
            } else {
                textArea.style.whiteSpace = "pre"
            }
        })
    })

    let contentLines = content.split("\n")

    const warn: Issue[] = []
    const severe: Issue[] = []

    for (let contentLine of contentLines) {
        if (reportBook === false) {
            continue;
        }

        for (let result of results) {
            if (contentLine.toLowerCase().includes(result.id.toLowerCase())) {
                if (result.severity === 1) {
                    warn.push(result)
                } else if (result.severity === 2) {
                    severe.push(result)
                }
            }
        }
    }

    if (scan) {
        warnings.set(warn)
        severes.set(severe)
    }

    if (canScan(contentLines)) validScan.set(true)

    function canScan(lines: string[]): boolean {
        for (let line of lines) {
            if (getLineSeverity(line) !== 0) {
                return true
            }

            if (reportBook !== true) continue
            for (let result of results) {
                if (line.toLowerCase().includes(result.id.toLowerCase())) {
                    return true
                }
            }
        }

        return false
    }

    function getLineSeverity(line: string): number {
        if (line.trim().toLowerCase().includes("[warn") ||
            line.trim().toLowerCase().includes("/warn]")) {
            return 1;
        } else if (line.trim().toLowerCase().includes("[severe]") ||
            line.trim().toLowerCase().includes("[error") ||
            line.trim().toLowerCase().includes("/error]") ||
            line.trim().toLowerCase().startsWith("caused by:") ||
            line.toLowerCase().startsWith("\tat") ||
            (line.toLowerCase().includes("exception") &&
                line.toLowerCase().includes("provided by"))) {
            return 2;
        }

        return 0;
    }

    function scanContent(content: String): number {
        if (!scan) {
            return 0;
        }

        if (reportBook === false) {
            return getLineSeverity(content)
        }

        for (let result of results) {
            if (content.toLowerCase().includes(result.id.toLowerCase())) {
                return result.severity;
            }
        }

        return 0;
    }

    function onInput(event: InputEvent) {
        writableContent.set((event.target as HTMLInputElement).value)
    }

    function getIndex(index: number): string {
        let max = contentLines.length
        let maxStringLength = max.toString().length
        return index.toString().padStart(maxStringLength, " ")
    }
</script>

<content-container class="new-{newReport}">
    {#if !newReport}
        <lines>
            {#each contentLines as line, index}
                <linecontainer class="wrap-{wrapPre}"><number class="number">{getIndex(index + 1)}</number><line-content-container class="severity-{scanContent(line)}">{line}</line-content-container></linecontainer>
            {/each}
        </lines>
    {:else}
        <textarea class="input" on:input="{onInput}" />
    {/if}
</content-container>

<style lang="scss">
  content-container {
    display: block;
    background-color: var(--container-background);
    width: calc(100% - 20px);
    margin: 10px;
    border-radius: 20px;
    animation: fadeIn 0.5s;
    animation-iteration-count: 1;
    animation-fill-mode: forwards;
    opacity: 0;
    height: calc(100% - 140px);
    overflow-x: scroll;
    border: 1px solid var(--container-border);

    @media (max-width: 600px) {
      height: calc(100% - 130px);
      margin-top: 6px;
    }

    &.new-true {
      height: calc(100% - 175px);
    }

    transition: all 0.5s ease;
  }

  .input {
    display: inline-block;
    border: none;
    width: calc(100% - 60px);
    height: calc(100% - 64px);
    background-color: transparent;
    font-size: 13px;
    font-family: "JetBrains Mono NL", monospace;
    outline: none;
    resize: none;
    margin: 30px;
    padding: 0;
    color: var(--container-text);

    @media (max-width: 600px){
      font-size: 10px;
    }
  }

  linecontainer {
    display: block;
    &:hover {
      .number {
        color: var(--content-line-number-hover);
      }
    }
  }

  lines {
    display: block;
    padding: 30px 30px 30px 18px;
    height: calc(100% - 60px);
  }

  .number {
    display: inline-block;
    text-align: right;
    padding-right: 20px;
    color: var(--content-line-number);
    font-family: "JetBrains Mono NL", monospace;
    font-size: 13px;
    margin: 0;
    opacity: 1;
    user-select: none;
    -webkit-user-select: none;

    &::selection {
      background-color: orange;
    }

    @media (max-width: 600px){
      font-size: 10px;
      padding-right: 12px;
    }

    transition: all 0.2s ease;
  }

  linecontainer {
    display: block;
    transition: color 0.2s ease;
    color: var(--container-text);

    font-size: 13px;
    white-space: pre;
    font-family: "JetBrains Mono NL", monospace;
    margin: 0;
    padding-left: 51px;
    text-indent: -26px;

    &.wrap-true {
      white-space: break-spaces;
    }

    @media (max-width: 600px){
      font-size: 11px;
      padding-left: 30px;
      text-indent: -18px;
    }
  }

  line-content-container {
    margin-right: 30px;
    &.severity-1 {
      background-color: var(--content-severity-warning);
    }

    &.severity-2 {
      background-color: var(--content-severity-error);
    }
  }

  @keyframes fadeIn {
    0% {
      opacity: 0;
    }
    100% {
      opacity: 1;
    }
  }
</style>
