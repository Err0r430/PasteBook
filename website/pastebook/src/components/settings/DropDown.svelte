<script lang="ts">
  import Select from 'svelte-select';
    export let callback: (value: any) => void;
    export let items: { value: any, label: string }[] = [];
    export let value: any;

    function handleCallback(event: any) {
        callback(event.detail.value);
    }

    function findLabel(value: any) {
        return items.find((item) => item.value === value)?.label;
    }
</script>

<dropdown>
  <Select items={items} floatingConfig={{
            strategy: 'fixed',
        }} 
        --item-is-active-bg={'var(--components-dropdown-selected)'}
        --item-hover-bg={'var(--components-dropdown-hover)'}
        --item-color={'var(--components-dropdown-text)'}
        --item-is-active-color={'var(--components-dropdown-text)'}
        --list-border={'1px solid var(--components-dropdown-border) 10px'}
        listOffset={5}
        inputStyles={'cursor: pointer;'}
        containerStyles={'width: 150px; color: var(--components-dropdown-text); background-color: var(--components-dropdown-background); border-radius: 30px; height: 40px; font-size: 15px; padding: 5px 5px 5px 15px; margin: 10px; border: 1px solid var(--components-dropdown-border); font-family: Gabarito, sans-serif; webkit-appearance: none; &:hover { cursor: pointer; background-color: var(--components-dropdown-hover); } &:focus { outline: none; } &:active { background-color: var(--components-dropdown-active); } overflow: visible;'} 
        on:change={handleCallback} showChevron value={findLabel(value)} searchable={false} clearable={false} />
        
</dropdown>

<style lang="scss">
  :global(.list-item) {
    background-color: var(--components-dropdown-background);
  }
  :global(.svelte-select) {
    cursor: pointer !important;

    &:hover {
      background-color: var(--components-dropdown-hover) !important;
    }
  }
</style>