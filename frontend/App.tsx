import {NavigationContainer} from "@react-navigation/native";
import RootNavigator from "./src/navigation/RootNavigator.tsx";
import { AuthProvider } from "./src/context/AuthContext.tsx";
import { SafeAreaProvider } from "react-native-safe-area-context";

function App() {
  return (
    <AuthProvider>
      <SafeAreaProvider>
        <NavigationContainer>
          <RootNavigator />
        </NavigationContainer>
      </SafeAreaProvider>
    </AuthProvider>
  );
}

export default App;
