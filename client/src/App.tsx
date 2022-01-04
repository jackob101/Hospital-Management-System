import React from "react";

const test = {
  name: "Johny",
  lastName: "Antony",
  age: 24,
};

function App() {
  return (
    <div className="App">
      {test.name + " " + test.lastName + " " + test.age}
    </div>
  );
}

export default App;
