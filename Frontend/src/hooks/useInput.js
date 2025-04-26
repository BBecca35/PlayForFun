import retrieveLocalData from "./useLocalStorage";

const useInput = (key, initValue) => {
    const [value, setValue] = retrieveLocalData(key, initValue);

    const reset = () => setValue(initValue);

    const attributeObj = {
        value,
        onChange: (e) => setValue(e.target.value)
    }

    return [value, reset, attributeObj];
}

export default useInput 